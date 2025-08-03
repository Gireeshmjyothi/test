plugins {
    id 'java'
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

shadowJar {
    archiveBaseName.set('your-job-name')
    archiveClassifier.set('')
    archiveVersion.set('1.0')
    mergeServiceFiles()
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes 'Main-Class': 'com.yourpackage.MainApp'
    }
}        }
    }

    public void publishDuplicate(List<TransactionData> duplicateList) {
        try (KafkaProducer<String, String> producer = createProducer()) {
            for (TransactionData data : duplicateList) {
                String json = objectMapper.writeValueAsString(data);
                producer.send(new ProducerRecord<>(config.getTopicDuplicate(), json));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

