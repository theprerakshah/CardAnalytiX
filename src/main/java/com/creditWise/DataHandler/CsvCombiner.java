package com.creditWise.DataHandler;

import java.io.*;
import java.util.*;

public class CsvCombiner
{
	public static void main(String[] args)
	{
		// List of input CSV file paths
		String[] inputFiles = {"CIBC_scraped_data.csv", "file2.csv", "file3.csv", "file4.csv"};

		// Output CSV file path
		String outputFile = "combined_card_dataset.csv";

		// Columns to maintain
		String[] headers = {"Card Names", "Card Category", "Annual Fee", "Purchase Interest Rate", "Cash Interest Rate", "Benefits"};

		// Set to track written rows to avoid duplicates (optional)
		Set<String> writtenRows = new HashSet<>();

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)))
		{
			// Write the header to the output file
			writer.write(String.join(",", headers));
			writer.newLine();

			// Process each input file
			for(String inputFile : inputFiles)
			{
				try(BufferedReader reader = new BufferedReader(new FileReader(inputFile)))
				{
					String line;
					boolean isHeader = true;

					while((line = reader.readLine()) != null)
					{
						// Skip the header line in each input file
						if(isHeader)
						{
							isHeader = false;
							continue;
						}

						// Avoid writing duplicate rows (optional)
						if(writtenRows.contains(line))
						{
							continue;
						}

						// Write the row to the output file
						writer.write(line);
						writer.newLine();

						// Add to the set of written rows
						writtenRows.add(line);
					}
				}
				catch(IOException e)
				{
					System.err.println("Error reading file: " + inputFile + " - " + e.getMessage());
				}
			}

			System.out.println("Combined CSV created successfully: " + outputFile);
		}
		catch(IOException e)
		{
			System.err.println("Error writing to output file: " + outputFile + " - " + e.getMessage());
		}
	}
}
