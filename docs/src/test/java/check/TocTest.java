/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package check;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Check that each rst file is mentinoed in the toc.xml file.
 * 
 * @author Jody Garnett
 * @since 1.3.2
 */
public class TocTest {

    @Test
    public void pageCheck() throws Exception {
        File docs = new File(".");
        File toc = new File( new File( docs, "user" ), "toc.xml");
        assertTrue( "toc.xml found", toc.exists() );
        
        String line;
        Matcher matcher;
        
        // Test with http://www.regexplanet.com/advanced/java/index.html
        // .*href="EN/(.+?).html".*
        Pattern hrefPattern = Pattern.compile(".*href=\"(.+?).html\".*");
        line = "        <topic label=\"uDig Overview\" href=\"EN/uDig Overview.html\">";
        matcher = hrefPattern.matcher(line);
        assertTrue( "hrefPattern match", matcher.matches() );
        assertEquals("hrefPattern Page extract", "EN/uDig Overview", matcher.group(1) );
        
        // topic="EN/(.+?).html"
        Pattern topicPattern = Pattern.compile(".*topic=\"(.+?).html\".*");
        line = "<toc label=\"Users Guide\" topic=\"EN/index.html\">";
        matcher = topicPattern.matcher(line);
        assertTrue( "topicPattern match", matcher.matches() );
        assertEquals("topicPattern Page extract", "EN/index", matcher.group(1) );
        
        line = "<toc label=\"Users Guide\" topic=\"EN/concept/index.html\">";
        matcher = topicPattern.matcher(line);
        assertTrue( "topicPattern match", matcher.matches() );
        assertEquals("topicPattern Page extract", "EN/concept/index", matcher.group(1) );
        
        // scan contents of toc.xml file
        //
        Map<String,String> pages = new HashMap<String,String>();
        BufferedReader reader = new BufferedReader(new FileReader(toc));
        try {
            int number=0;
            while ((line = reader.readLine()) != null) {
                number++;
                
                matcher = hrefPattern.matcher(line);
                if( matcher.matches() ){
                    String page = matcher.group(1);
                    if( pages.containsKey( page )){
                        conflict( number, page, line, pages.get(page));
                    }
                    else {
                        pages.put(page, line);
                    }
                }
                matcher = topicPattern.matcher(line);
                if( matcher.matches() ){
                    String page = matcher.group(1);
                    if( pages.containsKey( page )){
                        conflict( number, page, line, pages.get(page));
                    }
                    else {
                        pages.put(page, line);
                    }
                }
            }
            // scan rst files!
            File en = new File( new File( docs, "user" ), "en");
            boolean check = checkDirectory( "EN",en, pages );
            
            assertTrue("Some RST pages not mentioned in toc.xml. Please review standard output for details",check);
        }
        finally {
            if( reader != null ) reader.close();
        }
        if( !pages.isEmpty() ){
            String failMessage = "toc.xml refers to "+pages.size()+" missing pages";
            System.out.println(failMessage);
            for( String missing : pages.values() ){
                System.out.println( missing );
            }
            fail( failMessage);
        }

    }

    private boolean checkDirectory(String path, File directory, Map<String, String> pages) {
        boolean check = true;
        for( File file : directory.listFiles() ){
            if( file.isDirectory() ){
                boolean checked = checkDirectory( path+"/"+file.getName(), file, pages );
                if( checked == false){
                    check = false;
                }
            }
            if( file.getName().endsWith(".rst")){
                String name = file.getName();
                name = name.substring(0,name.length()-4);
                
                String key = path+"/"+name;
                if( pages.containsKey(key)){
                    pages.remove(key); // checked!
                }
                else {
                    System.out.println("toc.xml does not reference '"+key+".html'");
                    
                    System.out.print("        <topic label=\"");
                    System.out.print( name );
                    System.out.print("\" href=\"");
                    System.out.print( path );
                    System.out.print( "/" );
                    System.out.print( name );
                    System.out.println( ".html\">");
                    System.out.println("                </topic>");
                    
                    check = false;
                }
            }
        }
        return check;
    }

    private void conflict(int number, String page, String reference, String origional) {
        System.out.print( "Line " );
        System.out.print( number );
        System.out.print( "  duplicate page reference for ");
        System.out.print( page );
        System.out.println(".rst");
        System.out.println( "origional:"+origional );
        System.out.println( "reference:"+reference );
    }

}
