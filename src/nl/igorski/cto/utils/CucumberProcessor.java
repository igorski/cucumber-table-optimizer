/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Igor Zinken
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package nl.igorski.cto.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CucumberProcessor
{
    private static String TABLE_DEFINITION = "^.*(Examples:).*$";

    public static void walk( File input, File output ) {

        recursiveRead( input, output );
    }

    /* private methods */

    private static void recursiveRead( File input, File outputRoot ) {
        recursiveRead( input, outputRoot, "" );
    }

    private static void recursiveRead( File input, File outputRoot, String subDir ) {

        for ( File file : input.listFiles()) {
            if ( file.isFile() && isCucumberFile( file )) {
                System.out.println( "Processing Cucumber file: " + subDir + file.getName() );
                processFile( file, outputRoot.getAbsolutePath() + subDir );
            }
            else if ( file.isDirectory()) {
                final String subDirectory = ( subDir.length() == 0 ) ? file.separator : subDir;
                recursiveRead( file, outputRoot, subDirectory + file.separator + file.getName() );
            }
        }
    }

    private static boolean isCucumberFile( File file ) {

        String extension = "";
        try {
            final String name = file.getName();
            extension = name.substring( name.lastIndexOf( "." ) + 1 );
        }
        catch ( Exception e ) {}
        return ( extension.equals( "feature" ));
    }

    private static void processFile( File cucumberFile, String outputFolder ) {

        // read the Files contents line by line

        final String[] lines = FileUtil.getFileContentsAsArray( cucumberFile );

        // Array that will contain all lines that describe the feature

        ArrayList<String> scenario = new ArrayList<String>();
        boolean foundTable = false;
        int rowNumber = 0;
        for ( int lineNumber = 0; lineNumber < lines.length; ++lineNumber ) {

            final String line = lines[ lineNumber ];

            if ( !foundTable ) {

                // find the definition of the table (is "Examples:" instruction)

                scenario.add( line );

                if ( !foundTable && isTableDefinition( line )) {
                    foundTable = true;
                    // add the next line into the scenario Array (defines table header and column names)
                    scenario.add( lines[ lineNumber + 1 ]);
                    ++lineNumber; // skip the table header line and go straight into the rows
                }
            }
            else {

                // table definition found, read line by line and generate files for each line

                if ( isTableRow( line )) {

                    // clone the scenario data and add the row data
                    ArrayList<String> data = ( ArrayList<String> ) scenario.clone();
                    data.add( line );

                    ++rowNumber;

                    FileUtil.generateFile(
                        // file will have a number indicating which row is being described
                        cucumberFile.getName().replace( ".feature", "_" + rowNumber + ".feature" ),
                        data, outputFolder
                    );
                }
            }
        }
    }

    private static boolean isTableDefinition( String line ) {
        return line.matches( TABLE_DEFINITION );
    }

    private static boolean isTableRow( String line ) {
        return line.contains( "|" );
    }
}
