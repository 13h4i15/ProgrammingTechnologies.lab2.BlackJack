package servermodel.file;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionFinalTest {

    @Test
    public void sessionFinalSave() throws IOException, ProcessingException {
        List<Boolean> isLineCorrect = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        try (BufferedReader reader = new BufferedReader(new FileReader("results.txt"))) {
            String schemaString = mapper.writeValueAsString(schemaGen.generateSchema(SessionFinal.class));
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonNode raw = JsonLoader.fromString(schemaString);
            JsonSchema schema = factory.getJsonSchema(raw);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    schema.validate(JsonLoader.fromString(line));
                    isLineCorrect.add(true);
                } catch (JsonParseException e) {
                    isLineCorrect.add(false);
                }
            }
        }

        Assert.assertArrayEquals(isLineCorrect.stream().map(value -> true).toArray(), isLineCorrect.toArray());
    }
}
