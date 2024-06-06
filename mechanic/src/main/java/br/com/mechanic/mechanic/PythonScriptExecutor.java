//package br.com.mechanic.mechanic;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.nio.file.Paths;
//
//public class PythonScriptExecutor {
//    public static void main(String[] args) {
//        try {
//            // Caminho absoluto para o script Python
//            String scriptPath = Paths.get("src", "main", "resources", "scripts", "fetch_popular_cars_and_save_to_db.py").toAbsolutePath().toString();
//
//            // Comando para executar o script Python
//            String[] command = new String[]{"python", scriptPath};
//
//            // Executa o script
//            ProcessBuilder pb = new ProcessBuilder(command);
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            // Lê a saída do script
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // Espera o processo terminar
//            int exitCode = process.waitFor();
//            System.out.println("Script exited with code: " + exitCode);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
