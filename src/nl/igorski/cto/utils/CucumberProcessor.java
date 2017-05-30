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

    /**
     * recursively walk through all directories of given inputDir, processing
     * all Cucumber .feature files inside and splitting them into separate
     * files per table row (if the file uses tables)
     *
     * @param inputDir the directory containing all Cucumber files, will be traversed recursively
     * @param outputDir the output directory in which to write the transformed Cucumber files
     */
    public static void walk( File inputDir, File outputDir ) {

        recursiveRead( inputDir, outputDir );
    }

    /* private methods */

    /**
     * @param input the directory containing all Cucumber files, will be traversed recursively
     * @param outputRoot the output directory in which to write the transformed Cucumber files
     */
    private static void recursiveRead( File input, File outputRoot ) {
        recursiveRead( input, outputRoot, "" );
    }

    /**
     * @param input the directory containing all Cucumber files, will be traversed recursively
     * @param outputRoot the output directory in which to write the transformed Cucumber files
     * @param subDir subdirectory of outputRoot in which to write the transformed Cucumber files
     */
    private static void recursiveRead( File input, File outputRoot, String subDir ) {

        for ( File file : input.listFiles()) {
            if ( file.isFile() && FileUtil.isCucumberFile( file )) {
                System.out.println( "Processing Cucumber file: " + subDir + file.getName() );
                processFile( file, outputRoot.getAbsolutePath() + subDir );
            }
            else if ( file.isDirectory()) {
                final String subDirectory = ( subDir.length() == 0 ) ? file.separator : subDir;
                recursiveRead( file, outputRoot, subDirectory + file.separator + file.getName() );
            }
        }
    }

    /**
     * @param cucumberFile the Cucumber file to process
     * @param outputFolder  the output directory in which to write the transformed Cucumber files
     */
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

                    FileUtil.writeFile(
                        // file will have a number indicating which row is being described
                        cucumberFile.getName().replace( ".feature", "_" + rowNumber + ".feature" ),
                        data, outputFolder
                    );
                }
            }
        }

        if ( !foundTable )
            FileUtil.copyFile( cucumberFile, outputFolder );
    }

    /**
     * check whether given string contains a definition
     * for a Cucumber table
     *
     * @param line
     * @return
     */
    private static boolean isTableDefinition( String line ) {
        return line.matches( TABLE_DEFINITION );
    }

    /**
     * check whether given string describes a row
     * in a Cucumber table
     *
     * @param line
     * @return
     */
    private static boolean isTableRow( String line ) {
        return line.contains( "|" );
    }
}
