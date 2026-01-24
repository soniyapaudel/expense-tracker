package com.soniya.report;

import com.soniya.expensetracker.model.Expense;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class ExpensePDFReport {

    public static void generate(List<Expense> expenses, String filePath) {
        if (expenses == null || expenses.isEmpty()) {
            System.out.println("No expenses to generate report.");
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Title
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(220, 750);
            contentStream.showText("Expense Report");
            contentStream.endText();

            // Table Headers
            float yStart = 700;
            float margin = 50;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 25;
            float tableBottomY = 100;

            // columns
            String[] headers = { "No.", "Amount", "Category", "Description", "Date" };
            float[] colWidths = { 40, 80, 100, 200, 80 };

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Draw header background

            contentStream.setNonStrokingColor(200, 200, 200);
            contentStream.addRect(margin, yPosition - rowHeight, tableWidth, rowHeight);
            contentStream.fill();
            contentStream.setNonStrokingColor(0, 0, 0);

            // Draw header text

            float textX = margin + 2;
            float textY = yPosition - 18;
            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(textX, textY);
                contentStream.showText(headers[i]);
                contentStream.endText();
                textX += colWidths[i];
            }

            // Draw header border

            drawRowBorder(contentStream, margin, yPosition, rowHeight, tableWidth);

            yPosition -= rowHeight;

            // Draw rows
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            int count = 1;
            for (Expense e : expenses) {
                if (yPosition <= tableBottomY) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = yStart;
                }

                // Draw row background

                if (count % 2 == 0) {
                    contentStream.setNonStrokingColor(230, 230, 230);
                    contentStream.addRect(margin, yPosition - rowHeight, tableWidth, rowHeight);
                    contentStream.fill();
                    contentStream.setNonStrokingColor(0, 0, 0);

                }

                // Draw row text

                String[] row = {
                        String.valueOf(count),
                        String.format("%.2f", e.getAmount()),
                        e.getCategory(),
                        e.getDescription(),
                        e.getDate().toString()

                };
                textX = margin + 2;
                textY = yPosition - 18;
                for (int i = 0; i < row.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(textX, textY);
                    contentStream.showText(row[i]);
                    contentStream.endText();
                    textX += colWidths[i];
                }
                // Draw row border
                drawRowBorder(contentStream, margin, yPosition, rowHeight, tableWidth);

                yPosition -= rowHeight;
                count++;

            }
            contentStream.close();
            document.save(filePath);
            System.out.println("Expense report generated at: " + filePath);

        } catch (IOException ex) {
            System.out.println("Error generating PDF: " + ex.getMessage());
        }
    }

    // Helper to draw horizontal line for a row
    private static void drawRowBorder(PDPageContentStream contentStream, float x, float y, float height, float width)
            throws IOException {
        contentStream.setStrokingColor(0, 0, 0);
        contentStream.addRect(x, y - height, width, height);
        contentStream.stroke();
    }

}
