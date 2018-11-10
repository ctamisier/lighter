package io.lighter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lighter.exception.InvalidHighlight;
import io.lighter.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LighterTest {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private Customer customer;

    @BeforeEach
    public void beforeEach() {
        customer = new Customer();
        Infos infos = new Infos();
        SubInfos subInfos = new SubInfos();
        Address mainAddress = new Address();
        Address secondaryAddress = new Address();

        customer.setLastname("DoeDoe");
        customer.setFirstname("John");

        infos.setInfosValue("Some additional infos");
        infos.setBackReference(customer);
        infos.setParentInfos("parent infos about Doe");

        subInfos.setSubInfosValue("sub infos on DOE");
        infos.setMoreInfos(subInfos);

        mainAddress.setStreet("Doe street");
        mainAddress.setCity("New Taipei");

        secondaryAddress.setStreet("Nunuapa");
        secondaryAddress.setCity("San Salvador");

        customer.setInfos(infos);
        customer.setAddresses(new HashSet<>(Arrays.asList(mainAddress, secondaryAddress)));

        customer.setEmails(Arrays.asList("john.doe@gmail.com", "john.doe@yahoo.com"));

        System.out.println(gson.toJson(customer));
    }

    @Test
    public void testInvalidHighlight() {
        assertThrows(InvalidHighlight.class, () -> new Lighter.Builder(customer, null)
                .build()
                .highlight());

        assertThrows(InvalidHighlight.class, () -> new Lighter.Builder(customer, "")
                .build()
                .highlight());
    }

    @Test
    public void testFields() {
        List<Highlight> highlights = new Lighter.Builder(customer, "doe")
                .fields("addresses.street")
                .build()
                .highlight();

        System.out.println(gson.toJson(highlights));

        int idx = 0;
        Highlight addressFV = highlights.get(idx++);

        assertEquals(idx, highlights.size());

        assertEquals("doe", addressFV.getHl());
        assertEquals("street", addressFV.getPathFieldName());
        assertEquals("addresses[1].street", addressFV.getFullPathFieldName());
        assertEquals("Doe street", addressFV.getOriginalValue());
        assertEquals("Doe street", addressFV.getValue());
    }

    @Test
    public void testDefault() {
        List<Highlight> highlights = new Lighter.Builder(customer, "doe")
                .build()
                .highlight();

        System.out.println(gson.toJson(highlights));

        int idx = 0;
        Highlight addressFV = highlights.get(idx++);
        Highlight emailFV1 = highlights.get(idx++);
        Highlight emailFV2 = highlights.get(idx++);
        Highlight subInfosFV = highlights.get(idx++);
        Highlight parentInfosFV = highlights.get(idx++);
        Highlight lastnameFV = highlights.get(idx++);

        assertEquals(idx, highlights.size());

        assertEquals("street", addressFV.getPathFieldName());
        assertEquals("addresses[1].street", addressFV.getFullPathFieldName());
        assertEquals("Doe street", addressFV.getOriginalValue());
        assertEquals("Doe street", addressFV.getValue());

        assertEquals("emails[0]", emailFV1.getPathFieldName());
        assertEquals("emails[0]", emailFV1.getFullPathFieldName());
        assertEquals("john.doe@gmail.com", emailFV1.getOriginalValue());
        assertEquals("john.doe@gmail.com", emailFV1.getValue());

        assertEquals("emails[1]", emailFV2.getPathFieldName());
        assertEquals("emails[1]", emailFV2.getFullPathFieldName());
        assertEquals("john.doe@yahoo.com", emailFV2.getOriginalValue());
        assertEquals("john.doe@yahoo.com", emailFV2.getValue());

        assertEquals("subInfosValue", subInfosFV.getPathFieldName());
        assertEquals("infos.moreInfos.subInfosValue", subInfosFV.getFullPathFieldName());
        assertEquals("sub infos on DOE", subInfosFV.getOriginalValue());
        assertEquals("sub infos on DOE", subInfosFV.getValue());

        assertEquals("parentInfos", parentInfosFV.getPathFieldName());
        assertEquals("infos.parentInfos", parentInfosFV.getFullPathFieldName());
        assertEquals("parent infos about Doe", parentInfosFV.getOriginalValue());
        assertEquals("parent infos about Doe", parentInfosFV.getValue());

        assertEquals("lastname", lastnameFV.getPathFieldName());
        assertEquals("lastname", lastnameFV.getFullPathFieldName());
        assertEquals("DoeDoe", lastnameFV.getOriginalValue());
        assertEquals("DoeDoe", lastnameFV.getValue());
    }

    @Test
    public void testIgnoredClass() {
        List<Highlight> highlights = new Lighter.Builder(customer, "doe")
                .ignoredClasses(SuperInfos.class)
                .build()
                .highlight();

        System.out.println(gson.toJson(highlights));

        int idx = 0;
        Highlight addressFV = highlights.get(idx++);
        Highlight emailFV1 = highlights.get(idx++);
        Highlight emailFV2 = highlights.get(idx++);
        Highlight subInfosFV = highlights.get(idx++);
        Highlight lastnameFV = highlights.get(idx++);

        assertEquals(idx, highlights.size());

        assertEquals("street", addressFV.getPathFieldName());
        assertEquals("addresses[1].street", addressFV.getFullPathFieldName());
        assertEquals("Doe street", addressFV.getOriginalValue());
        assertEquals("Doe street", addressFV.getValue());

        assertEquals("emails[0]", emailFV1.getPathFieldName());
        assertEquals("emails[0]", emailFV1.getFullPathFieldName());
        assertEquals("john.doe@gmail.com", emailFV1.getOriginalValue());
        assertEquals("john.doe@gmail.com", emailFV1.getValue());

        assertEquals("emails[1]", emailFV2.getPathFieldName());
        assertEquals("emails[1]", emailFV2.getFullPathFieldName());
        assertEquals("john.doe@yahoo.com", emailFV2.getOriginalValue());
        assertEquals("john.doe@yahoo.com", emailFV2.getValue());

        assertEquals("subInfosValue", subInfosFV.getPathFieldName());
        assertEquals("infos.moreInfos.subInfosValue", subInfosFV.getFullPathFieldName());
        assertEquals("sub infos on DOE", subInfosFV.getOriginalValue());
        assertEquals("sub infos on DOE", subInfosFV.getValue());

        assertEquals("lastname", lastnameFV.getPathFieldName());
        assertEquals("lastname", lastnameFV.getFullPathFieldName());
        assertEquals("DoeDoe", lastnameFV.getOriginalValue());
        assertEquals("DoeDoe", lastnameFV.getValue());
    }

    @Test
    public void testEmphasisAndIgnoredClass() {
        List<Highlight> highlights = new Lighter.Builder(customer, "doe")
                .htmlTag("em")
                .ignoredClasses(SuperInfos.class)
                .build()
                .highlight();

        System.out.println(gson.toJson(highlights));

        int idx = 0;
        Highlight addressFV = highlights.get(idx++);
        Highlight emailFV1 = highlights.get(idx++);
        Highlight emailFV2 = highlights.get(idx++);
        Highlight subInfosFV = highlights.get(idx++);
        Highlight lastnameFV = highlights.get(idx++);

        assertEquals(idx, highlights.size());

        assertEquals("street", addressFV.getPathFieldName());
        assertEquals("addresses[1].street", addressFV.getFullPathFieldName());
        assertEquals("Doe street", addressFV.getOriginalValue());
        assertEquals("<em>Doe</em> street", addressFV.getValue());

        assertEquals("emails[0]", emailFV1.getPathFieldName());
        assertEquals("emails[0]", emailFV1.getFullPathFieldName());
        assertEquals("john.doe@gmail.com", emailFV1.getOriginalValue());
        assertEquals("john.<em>doe</em>@gmail.com", emailFV1.getValue());

        assertEquals("emails[1]", emailFV2.getPathFieldName());
        assertEquals("emails[1]", emailFV2.getFullPathFieldName());
        assertEquals("john.doe@yahoo.com", emailFV2.getOriginalValue());
        assertEquals("john.<em>doe</em>@yahoo.com", emailFV2.getValue());

        assertEquals("subInfosValue", subInfosFV.getPathFieldName());
        assertEquals("infos.moreInfos.subInfosValue", subInfosFV.getFullPathFieldName());
        assertEquals("sub infos on DOE", subInfosFV.getOriginalValue());
        assertEquals("sub infos on <em>DOE</em>", subInfosFV.getValue());

        assertEquals("lastname", lastnameFV.getPathFieldName());
        assertEquals("lastname", lastnameFV.getFullPathFieldName());
        assertEquals("DoeDoe", lastnameFV.getOriginalValue());
        assertEquals("<em>Doe</em><em>Doe</em>", lastnameFV.getValue());
    }

    @Test
    public void testEmphasis() {
        List<Highlight> highlights = new Lighter.Builder(customer, "doe")
                .htmlTag("em")
                .build()
                .highlight();

        System.out.println(gson.toJson(highlights));

        int idx = 0;
        Highlight addressFV = highlights.get(idx++);
        Highlight emailFV1 = highlights.get(idx++);
        Highlight emailFV2 = highlights.get(idx++);
        Highlight subInfosFV = highlights.get(idx++);
        Highlight parentInfosFV = highlights.get(idx++);
        Highlight lastnameFV = highlights.get(idx++);

        assertEquals(idx, highlights.size());

        assertEquals("street", addressFV.getPathFieldName());
        assertEquals("addresses[1].street", addressFV.getFullPathFieldName());
        assertEquals("Doe street", addressFV.getOriginalValue());
        assertEquals("<em>Doe</em> street", addressFV.getValue());

        assertEquals("emails[0]", emailFV1.getPathFieldName());
        assertEquals("emails[0]", emailFV1.getFullPathFieldName());
        assertEquals("john.doe@gmail.com", emailFV1.getOriginalValue());
        assertEquals("john.<em>doe</em>@gmail.com", emailFV1.getValue());

        assertEquals("emails[1]", emailFV2.getPathFieldName());
        assertEquals("emails[1]", emailFV2.getFullPathFieldName());
        assertEquals("john.doe@yahoo.com", emailFV2.getOriginalValue());
        assertEquals("john.<em>doe</em>@yahoo.com", emailFV2.getValue());

        assertEquals("subInfosValue", subInfosFV.getPathFieldName());
        assertEquals("infos.moreInfos.subInfosValue", subInfosFV.getFullPathFieldName());
        assertEquals("sub infos on DOE", subInfosFV.getOriginalValue());
        assertEquals("sub infos on <em>DOE</em>", subInfosFV.getValue());

        assertEquals("parentInfos", parentInfosFV.getPathFieldName());
        assertEquals("infos.parentInfos", parentInfosFV.getFullPathFieldName());
        assertEquals("parent infos about Doe", parentInfosFV.getOriginalValue());
        assertEquals("parent infos about <em>Doe</em>", parentInfosFV.getValue());

        assertEquals("lastname", lastnameFV.getPathFieldName());
        assertEquals("lastname", lastnameFV.getFullPathFieldName());
        assertEquals("DoeDoe", lastnameFV.getOriginalValue());
        assertEquals("<em>Doe</em><em>Doe</em>", lastnameFV.getValue());
    }
}
