package com.creditWise.CardAnalytiX;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

public class Webcrawler {

    private static final String SAVE_DIRECTORY = "saved_pages";
    private static final int MAX_PAGES_PER_SITE = 100; // Limit for each site

    // Method to read URLs from a file
    public List<String> readUrlsFromFile(String filePath) {
        List<String> urls = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    urls.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading URLs from file: " + e.getMessage());
        }
        return urls;
    }

    // Method to start crawling a single site and fetch data only from provided URLs
    public void crawlSingleSite(String baseUrl) {
        Set<String> visitedPages = new HashSet<>();
        Queue<String> pagesToVisit = new LinkedList<>();
        pagesToVisit.add(baseUrl); // Only add the base URL to the queue

        // Start crawling
        while (!pagesToVisit.isEmpty() && visitedPages.size() < MAX_PAGES_PER_SITE) {
            String url = pagesToVisit.poll();

            // Only visit the page if it hasn't been visited before
            if (url != null && !visitedPages.contains(url)) {
                visitPage(url, visitedPages); // Visit the page without adding new URLs to the queue
            }
        }
    }

    // Method to visit a page, extract its data, and save it
    private void visitPage(String url, Set<String> visitedPages) {
        try {
            // Skip invalid or javascript:void(0) links
            if (url == null || url.isEmpty() || url.startsWith("javascript:void(0)")) {
                return;
            }

            // Fetch the document from the URL
            Document doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0")
            .header("Accept-Language", "en-US,en;q=0.9")
            .header("Accept-Encoding", "gzip, deflate, br")
            .timeout(30_000)
            .get();

            System.out.println("Visiting: " + url);
            visitedPages.add(url); // Mark the URL as visited

            // Save the HTML content of the page to a file
            saveHtmlToFile(doc, url);

        } catch (IOException e) {
            System.err.println("Error accessing: " + url + " - " + e.getMessage());
        }
    }

    // Method to save HTML content to a file
    private void saveHtmlToFile(Document doc, String url) {
        String fileName = url.replaceAll("[^a-zA-Z0-9]", "_") + ".html";
        File file = new File(SAVE_DIRECTORY, fileName);

        // Create directory if it doesn't exist
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(doc.outerHtml());
            System.out.println("Saved page to: " + file.getPath());
        } catch (IOException e) {
            System.err.println("Error saving HTML to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Webcrawler crawler = new Webcrawler();
        List<String> urls = crawler.readUrlsFromFile("src/urls.txt");

        for (String baseUrl : urls) {
            System.out.println("Starting crawl for: " + baseUrl);
            crawler.crawlSingleSite(baseUrl); // Crawl each URL
        }
    }
}
