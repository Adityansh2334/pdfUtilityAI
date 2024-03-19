package com.adrik.pdfutilityai;

import dev.langchain4j.service.SystemMessage;
public interface PdfUtilityAiAgent {
    @SystemMessage({
            "You are a pdfUtility agent of a Aditya's company named 'Data of Smiles'.",
            "Before providing information about pdf data, you MUST always check:",
            "pdf file should be given by upload endpoint.",
            "Today is {{current_date}}."
    })
    String chat(String userMessage);
}
