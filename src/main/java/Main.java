import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, ParseException {

        // 1е задание CSV-JSON парсер
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCVS(fileName, columnMapping);

        String json = listToJson(list);

        writeString(json, "data.json");


        // 2е задание XML-JSON парсер
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(new File("data.xml"));
//        List<Employee> list1 = parseXML(doc);
//        String json2 = listToJson(list1);
//
//        writeString(json2, "data2.json");


        // 3е задание JSON парсер
//        String a = readString();
//        List<Employee> list2 = jsonToList(a);
//        list2.forEach(System.out::println);

    }

    static List<Employee> parseCVS(String fileName, String[] column) {

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(column);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static String listToJson(List<Employee> list) {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();

        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("employee");
        List<Employee> emp = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                String fN = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lN = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                emp.add(new Employee(id, fN, lN, country, age));
            }
        }
        return emp;
    }

    public static String readString() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new BufferedReader(new FileReader("data.json")));
            JSONArray jsonArray = (JSONArray) obj;
            return jsonArray.toString();
        } catch (IOException | ParseException w) {
            w.printStackTrace();
        }
        return null;
    }

    public static List<Employee> jsonToList(String a) throws ParseException {
        JSONParser parser = new JSONParser();
        List<Employee> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) parser.parse(a);
        for (Object obj : jsonArray) {
            JSONObject job = (JSONObject) obj;
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Employee employee = gson.fromJson(String.valueOf(job), Employee.class);
            list.add(employee);
        }
        return list;
    }
}

