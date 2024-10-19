import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class PolynomialConstantFinder {
    public static void main(String[] args) {
        // Input JSON string
        String inputString = "{\"keys\": {\"n\": 4, \"k\": 3}, \"1\": {\"base\": \"10\", \"value\": \"4\"}, \"2\": {\"base\": \"2\", \"value\": \"111\"}, \"3\": {\"base\": \"10\", \"value\": \"12\"}, \"6\": {\"base\": \"4\", \"value\": \"213\"}}";

        // Extract points from the input string
        Map<Integer, Integer> points = extractPoints(inputString);
        if (points.isEmpty()) {
            System.out.println("No valid points found.");
            return;
        }

        // Find the constant term using the points
        double constantTerm = findConstantTerm(points);
        System.out.println("Constant term: " + constantTerm);
    }

    private static Map<Integer, Integer> extractPoints(String inputString) {
        Map<Integer, Integer> points = new HashMap<>();

        try {
            // Parse the input string as a JSON object
            JSONObject jsonObject = new JSONObject(inputString);

            // Extract 'keys' object
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            for (String key : jsonObject.keySet()) {
                // Skip the "keys" object
                if (!key.equals("keys")) {
                    JSONObject point = jsonObject.getJSONObject(key);
                    int base = Integer.parseInt(point.getString("base")); // Validate this input
                    String valueStr = point.getString("value");
                    
                    // Validate base before using it
                    if (base < 2 || base > 36) {
                        System.out.println("Base must be between 2 and 36. Found: " + base);
                        continue;
                    }
                    
                    int y = Integer.parseInt(valueStr, base);
                    int x = Integer.parseInt(key);  // Use the key as x value
                    points.put(x, y);
                }
            }
        } catch (JSONException e) {
            System.out.println("Error parsing JSON input: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from input: " + e.getMessage());
            e.printStackTrace();
        }

        return points;
    }

    private static double findConstantTerm(Map<Integer, Integer> points) {
        int n = points.size();
        double constantTerm = 0;

        // Convert points to arrays
        Integer[] xValues = points.keySet().toArray(new Integer[0]);
        Integer[] yValues = points.values().toArray(new Integer[0]);

        // Compute the Lagrange polynomial and evaluate at x = 0
        for (int i = 0; i < n; i++) {
            double term = yValues[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (0 - xValues[j]) / (double) (xValues[i] - xValues[j]);
                }
            }
            constantTerm += term;
        }

        return constantTerm;
    }
}
