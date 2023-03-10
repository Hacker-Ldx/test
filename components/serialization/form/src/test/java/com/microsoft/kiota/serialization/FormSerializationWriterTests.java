package com.microsoft.kiota.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.microsoft.kiota.serialization.mocks.TestEntity;

public class FormSerializationWriterTests {
	@Test
	public void writesSampleObjectValue() throws IOException {
		final var testEntity = new TestEntity() {{
			setId("48d31887-5fad-4d73-a9f5-3c356e68a038");
			setWorkDuration(Period.parse("P1M"));
			setStartWorkTime(LocalTime.of(8, 0, 0));
			setBirthDay(LocalDate.of(2017, 9, 4));
		}};
		testEntity.getAdditionalData().put("mobilePhone", null);
		testEntity.getAdditionalData().put("jobTitle", "Author");
		testEntity.getAdditionalData().put("accountEnabled", false);
		testEntity.getAdditionalData().put("createdDateTime", OffsetDateTime.MIN);
		try (final var serializationWriter = new FormSerializationWriter()) {
			serializationWriter.writeObjectValue(null, testEntity);
			try(final var content = serializationWriter.getSerializedContent()) {
				try(final var contentReader = new BufferedReader(new InputStreamReader(content))) {
					String result = contentReader.lines().collect(Collectors.joining("\n"));
					final var expectedString =    "id=48d31887-5fad-4d73-a9f5-3c356e68a038&" +
														"birthDay=2017-09-04&" + // Serializes dates
														"workDuration=P1M&"+    // Serializes timespans
														"startWorkTime=08%3A00%3A00&" + //Serializes times
														"mobilePhone=null&" + // Serializes null values
														"jobTitle=Author&" +
														"createdDateTime=-999999999-01-01T00%3A00%3A00%2B18%3A00&" +
														"accountEnabled=false";
					assertEquals(expectedString, result);
				}
			}
		}
	}
	@Test
	public void writesSampleCollectionOfObjectValues() throws IOException {
		final var testEntity = new TestEntity() {{
			setId("48d31887-5fad-4d73-a9f5-3c356e68a038");
			setWorkDuration(Period.parse("P1M"));
			setStartWorkTime(LocalTime.of(8, 0, 0));
			setBirthDay(LocalDate.of(2017, 9, 4));
		}};
		final var entityList = new ArrayList<TestEntity>() {{ add(testEntity); }};
		try (final var serializationWriter = new FormSerializationWriter()) {
			assertThrows(RuntimeException.class, () -> serializationWriter.writeCollectionOfObjectValues(null, entityList));
		}
	}
	@Test
	public void writesNestedObjectValuesInAdditionalData() throws IOException {
		final var testEntity = new TestEntity() {{
			setId("48d31887-5fad-4d73-a9f5-3c356e68a038");
			setWorkDuration(Period.parse("P1M"));
			setStartWorkTime(LocalTime.of(8, 0, 0));
			setBirthDay(LocalDate.of(2017, 9, 4));
		}};
		testEntity.getAdditionalData().put("nestedEntity", new TestEntity() {{
			setId("foo");
		}});
		try (final var serializationWriter = new FormSerializationWriter()) {
			assertThrows(RuntimeException.class, () -> serializationWriter.writeObjectValue(null, testEntity));
		}
	}
}
