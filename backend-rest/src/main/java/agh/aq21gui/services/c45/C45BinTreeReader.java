/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agh.aq21gui.services.c45;

import agh.aq21gui.j48treegrammar.J48Tree;
import agh.aq21gui.model.input.Attribute;
import agh.aq21gui.model.input.Input;
import agh.aq21gui.utils.Util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcin
 */
public class C45BinTreeReader {
    public static final int BrDiscr=1; /* node types:	branch */
    public static final int ThreshContin=2; /*		threshold cut */
    public static final int BrSubset=3; /*		subset test */
    private final Input experiment;
    private ArrayList<Integer> maxAttrVal;
    
    public C45BinTreeReader(Input experiment) {
        this.experiment = experiment;
        List<Attribute> attributes = experiment.getAttributes();
        maxAttrVal = new ArrayList<Integer>(/*attributes.size()*/);
        for (Attribute attr : attributes) {
            maxAttrVal.add(attr.set_elements.size());
        }
    }
    
    J48Tree getTree(String filename) {
        RandomAccessFile in=null;
        J48Tree tree = new J48Tree();
        try {
            //byte[] result = read(filename);
            in = new RandomAccessFile("filename", "r");
            C45Tree c45tree = inTree(in);
        } catch (IOException ex) {
            Logger.getLogger(C45BinTreeReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in!=null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(C45BinTreeReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return tree;
    }
    
    byte[] read(String aInputFileName) {
        File file = new File(aInputFileName);
        byte[] result = new byte[(int) file.length()];
        try {
            InputStream input = null;
            try {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while (totalBytesRead < result.length) {
                    int bytesRemaining = result.length - totalBytesRead;
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                    if (bytesRead > 0) {
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    public C45Tree inTree(RandomAccessFile in) throws IOException {
        C45Tree t = new C45Tree();
        //StreamIn((char *) &T->NodeType, sizeof(short));
        t.nodeType = in.readShort();
        //StreamIn((char *) &T->Leaf, sizeof(ClassNo));
        t.leaf = in.readShort();
        //StreamIn((char *) &T->Items, sizeof(ItemCount));
        t.items = in.readFloat();
        //StreamIn((char *) &T->Errors, sizeof(ItemCount));
        t.errors = in.readFloat();
        if (t.nodeType==0) {
            t.tested = in.readShort();
            t.forks = in.readShort();
            switch(t.nodeType) {
                case BrDiscr:
                    break;
                case ThreshContin:
                    t.cut = in.readFloat();
                    t.lower = in.readFloat();
                    t.upper = in.readFloat();
                    break;
                case BrSubset:
                    t.subset = new LinkedList<byte[]>();
                    int bytes = (maxAttrVal.get(t.tested)>>3)+1;
                    for (int v=1; v<=t.forks; ++v) {
                        byte[] subsetBytes = new byte[bytes];
                        in.readFully(subsetBytes);
                        t.subset.add(subsetBytes);
                    }
                    break;
            }
            t.branch = new LinkedList<C45Tree>();
            for (int v=1; v<=t.forks; ++v) {
                C45Tree child = inTree(in);
                t.branch.add(child);
            }
        }
        return t;
    }
    
    private class C45Tree {
        private short nodeType; /* 0=leaf 1=branch 2=cut 3=subset */
        private short leaf;  /* most frequent class at this node */
        private float errors; /* no of errors at this node */
        private float items;  /* no of items at this node */
        private short tested;
        private short forks; /* number of branches at this node */
        private float cut;  /* threshold for continuous attribute */
        private float lower; /* lower limit of soft threshold */
        private float upper; /* upper limit ditto */
        private List<C45Tree> branch; /* Branch[x] = (sub)tree for outcome x */
        private List<byte[]> subset; /* subsets of discrete values  */
    }
}
