/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.treewoods.sample_common_compress;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toru
 */
public class TarUtilTest {

    public TarUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of archive method, of class TarUtil.
     */
    @Test
    public void testArchive_String_String() throws Exception {
        System.out.println("archive");
        String baseDir = "./in/01/";
        String outputFile = "./out/01/test.tar";
        TarUtil instance = new TarUtil();
        instance.archiveFromDir(baseDir, null, outputFile, true);
    }


    /**
     * Test of archive method, of class TarUtil.
     */
    @Test
    public void testArchive() throws Exception {
        System.out.println("archive");
        String baseDir = "./in/02/";
        String[] inputFiles = {"file1", "file2", "dir/file3"};
        String outputFile = "./out/02/test.tar";
        TarUtil instance = new TarUtil();
        instance.archiveFromFiles(baseDir, inputFiles, outputFile);
    }

    /**
     * Test of extract method, of class TarUtil.
     */
    @Test
    public void testExtract() throws Exception {
        System.out.println("extract");
        String inputFile = "./in/03/test.tar";
        String outputDir = "./out/03";
        TarUtil instance = new TarUtil();
        instance.extract(inputFile, outputDir);
    }

}
