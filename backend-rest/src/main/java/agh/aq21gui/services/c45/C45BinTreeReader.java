/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.c45;

import agh.aq21gui.j48treegrammar.Branch;
import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.j48treegrammar.Node;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.model.input.Test;
import agh.aq21gui.model.output.ClassDescriptor;
import agh.aq21gui.utils.Util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class C45BinTreeReader {

    public static final int BrDiscr = 1; /* node types:	branch */

    public static final int ThreshContin = 2; /*		threshold cut */

    public static final int BrSubset = 3; /*		subset test */

    private final Input experiment;
    private ArrayList<Integer> maxAttrVal;
    private final int maxClass;
    private ClassDescriptor classDescr;
    private List<String> classRange;

    public C45BinTreeReader(Input experiment, Test test) {
        this.experiment = experiment;
        List<Attribute> attributes = experiment.getAttributes();
        maxAttrVal = new ArrayList<Integer>(/*attributes.size()*/);
        for (Attribute attr : attributes) {
            maxAttrVal.add(attr.set_elements.size());
        }
        classDescr = test.grepClassDescriptor();
        String className = classDescr.name;
        classRange = experiment.findDomainObjectRecursively(className).getRange();
        this.maxClass = classRange.size();
    }

    J48Tree getTree(String filename) {
        RandomAccessFile in = null;
        J48Tree tree = new J48Tree();
        C45Tree c45tree = null;
        try {
            //byte[] result = read(filename);
            in = new RandomAccessFile(filename, "r");
            FileChannel channel = in.getChannel();
            long size = channel.size();
            ByteBuffer buf = ByteBuffer.allocate((int) size);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            channel.read(buf);
            buf.flip();
            //It is noticeable that file begins with line-feed character
            buf.get();
            c45tree = inTree(buf);
            tree = convertC45TreeIntoJ48(c45tree);
        } catch (IOException ex) {
            Logger.getLogger(C45BinTreeReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(C45BinTreeReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return tree;
    }

    public C45Tree inTree(ByteBuffer in) throws IOException {
        C45Tree t = new C45Tree();
        //StreamIn((char *) &T->NodeType, sizeof(short));
        t.nodeType = in.getShort();
        //StreamIn((char *) &T->Leaf, sizeof(ClassNo));
        t.leaf = in.getShort();
        //StreamIn((char *) &T->Items, sizeof(ItemCount));
        t.items = in.getFloat();
        //StreamIn((char *) &T->Errors, sizeof(ItemCount));
        t.errors = in.getFloat();
        for (int i = 0; i < this.maxClass; ++i) {
            t.classDist.add(in.getFloat());
        }
        if (t.nodeType != 0) {
            t.tested = in.getShort();
            t.forks = in.getShort();
            switch (t.nodeType) {
                case BrDiscr:
                    break;
                case ThreshContin:
                    t.cut = in.getFloat();
                    t.lower = in.getFloat();
                    t.upper = in.getFloat();
                    break;
                case BrSubset:
                    int bytes = (maxAttrVal.get(t.tested) >> 3) + 1;
                    for (int v = 1; v <= t.forks; ++v) {
                        byte[] subsetBytes = new byte[bytes];
                        in.get(subsetBytes);
                        t.subset.add(subsetBytes);
                    }
                    break;
            }
            for (int v = 1; v <= t.forks; ++v) {
                C45Tree child = inTree(in);
                t.branch.add(child);
            }
        }
        return t;
    }

    private J48Tree convertC45TreeIntoJ48(C45Tree c45tree) {
        J48Tree result = new J48Tree();
        addNode(c45tree, "root", result);
        traverseTree(c45tree, result, "root", 1);
//        System.out.println(result.toString());
        result.fixupBranchAttributes();
        return result;
    }

    private int traverseTree(C45Tree parrent, J48Tree result, String parrentName, int enumerator) {
        ++enumerator;
        for (C45Tree child : parrent.branch) {
            String childName = String.format("N%s", enumerator);
            //visitBranch(parrent, child, parrentName, childName, result);
            addNode(child, childName, result);
            boolean isFirstChild = child == parrent.branch.get(0);
            addBranch(parrent, child, parrentName, childName, result, isFirstChild);
            enumerator = traverseTree(child, result, childName, enumerator);
        }
        return enumerator;
    }

    private void addBranch(C45Tree parrent, C45Tree child, String parrentName, String childName, J48Tree result, boolean firstChild) {
        if (parrent.nodeType == ThreshContin) {
            Branch branch = new Branch();
            branch.setFrom(parrentName);
            branch.setTo(childName);
            branch.setComparator(firstChild?"<=":">"); //TODO
            branch.setValue(String.valueOf(parrent.cut));
            result.addBranch(branch);
        } else if (parrent.nodeType == BrDiscr) {
            //TODO
            throw new RuntimeException("Unsupported BrDiscr with C4.5 algorithm");
        }
    }

    private void addNode(C45Tree child, String childName, J48Tree result) {
        Node node = new Node();
        if (child.branch.isEmpty()) {
            String classVal = this.classRange.get(child.leaf);
            node.setAttribute(classVal);
        } else {
            Attribute attr = experiment.getAttributes().get(child.tested);
            node.setAttribute(attr.name);
        }
        node.setName(childName);
        result.addNode(node);
    }

    public class C45Tree {

        private short nodeType; /* 0=leaf 1=branch 2=cut 3=subset */

        private short leaf;  /* most frequent class at this node */

        private float errors; /* no of errors at this node */

        private float items;  /* no of items at this node */

        private short tested;
        private short forks; /* number of branches at this node */

        private float cut;  /* threshold for continuous attribute */

        private float lower; /* lower limit of soft threshold */

        private float upper; /* upper limit ditto */

        private LinkedList<C45Tree> branch = new LinkedList<C45Tree>(); /* Branch[x] = (sub)tree for outcome x */

        private List<byte[]> subset = new LinkedList<byte[]>(); /* subsets of discrete values  */

        private List<Float> classDist = new LinkedList<Float>(); /* distribution of class values*/

    }
}
