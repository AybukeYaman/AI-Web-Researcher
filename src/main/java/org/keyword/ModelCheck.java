package org.keyword;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ModelCheck { 
    /*
    * THIS CLASS EXISTS TO SEE WHICH GEMINI VERSIONS ARE AVAILABLE WITHIN THE CURRENT KEY AND HAS NO OTHER FUNCTIONALITY.
    * THIS CLASS IS MADE ENTIRELY BY AI AND HAS NO PART IN REST OF THE CODE
    */


    // put API_KEY here
    private static final String API_KEY = "API_KEY"; 

    public static void main(String[] args) {
        System.out.println("🔍 Kullanılabilir modeller Google'dan isteniyor...");

        String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + API_KEY;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET() // Listelemek için GET isteği atılır
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("--- SUNUCU CEVABI ---");
            System.out.println("Durum Kodu: " + response.statusCode());
            System.out.println(response.body());
            System.out.println("---------------------");
            
            if (response.statusCode() == 200) {
                System.out.println("✅ İPUCU: Çıktıdaki 'name': 'models/xxxx' kısımlarına bak.");
                System.out.println("Örneğin: 'models/gemini-1.5-flash' veya 'models/gemini-pro' gördüğünü kopyala.");
            } else {
                System.out.println("❌ HATA: API anahtarında veya bağlantıda sorun olabilir.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
