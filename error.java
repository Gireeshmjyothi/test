 saveToReconciliationTable(matched, "MATCHED", true);
        saveToReconciliationTable(unmatched, "UNMATCHED", false);
        saveToReconciliationTable(sourceDuplicates, "SOURCE_DUPLICATE", false);
        saveToReconciliationTable(targetDuplicates, "TARGET_DUPLICATE", false);
