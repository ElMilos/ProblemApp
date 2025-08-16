package ProblemApp.demo.soap;

import ProblemApp.demo.dto.StatusCountResponse;
import ProblemApp.demo.service.ReportService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;

@Endpoint
public class IssueReportEndpoint {
    private static final String NAMESPACE = "http://acme.com/issueboard/report";
    private final ReportService reports;
    public IssueReportEndpoint(ReportService r){ this.reports=r; }

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetStatusCountsRequest")
    @ResponsePayload
    public Source getStatusCounts(@RequestPayload Source request) throws Exception {
        // Minimalny parser (prosty) — możesz też użyć JAXB i klas wygenerowanych.
        // Tu: ręcznie wyciągniemy wartości jako ciągi (dla MVP).
        // W realu: definicje JAXB (xjc) lub Spring-WS marshaller do klas.

        // Domyśl: ostatnie 30 dni, basedOn=created
        StatusCountResponse resp = reports.statusCount(null, null, "created");

        // Złóż prosty XML odpowiedzi
        StringBuilder sb = new StringBuilder();
        sb.append("<ns:GetStatusCountsResponse xmlns:ns=\"").append(NAMESPACE).append("\">");
        for (var it : resp.items()) {
            sb.append("<ns:item><ns:status>").append(it.status().name()).append("</ns:status>")
                    .append("<ns:count>").append(it.count()).append("</ns:count></ns:item>");
        }
        sb.append("</ns:GetStatusCountsResponse>");
        return new StringSource(sb.toString());
    }
}