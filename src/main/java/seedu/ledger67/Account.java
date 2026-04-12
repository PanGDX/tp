package seedu.ledger67;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Account {
    private static final List<String> VALID_ROOTS =
            Arrays.asList("assets", "liabilities", "equity", "income", "expenses");

    private final String fullName;
    private final List<String> hierarchy;

    public Account(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Account name cannot be empty.");
        }

        List<String> rawParts = Arrays.stream(fullName.trim().split(":"))
                .map(String::trim)
                .collect(Collectors.toList());

        if (rawParts.stream().anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("Account hierarchy cannot contain empty components.");
        }

        String normalizedRoot = normalizeRoot(rawParts.get(0));

        List<String> normalizedHierarchy = new java.util.ArrayList<>();
        normalizedHierarchy.add(normalizedRoot);

        for (int i = 1; i < rawParts.size(); i++) {
            normalizedHierarchy.add(rawParts.get(i));
        }

        this.hierarchy = normalizedHierarchy;
        this.fullName = String.join(":", normalizedHierarchy);
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getHierarchy() {
        return hierarchy;
    }

    public String getRoot() {
        return hierarchy.get(0).toLowerCase();
    }

    public boolean isUnder(String parentAccount) {
        if (parentAccount == null || parentAccount.isBlank()) {
            return false;
        }

        String normalizedParent = normalizeAccountName(parentAccount);
        String normalizedSelf = normalizeAccountName(fullName);

        String lowerParent = normalizedParent.toLowerCase();
        String lowerSelf = normalizedSelf.toLowerCase();

        return lowerSelf.equals(lowerParent)
                || lowerSelf.startsWith(lowerParent + ":");
    }

    private static String normalizeAccountName(String accountName) {
        List<String> parts = Arrays.stream(accountName.trim().split(":"))
                .map(String::trim)
                .collect(Collectors.toList());

        if (parts.isEmpty() || parts.stream().anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("Account hierarchy cannot contain empty components.");
        }

        String normalizedRoot = normalizeRoot(parts.get(0));

        List<String> normalizedParts = new java.util.ArrayList<>();
        normalizedParts.add(normalizedRoot);

        for (int i = 1; i < parts.size(); i++) {
            normalizedParts.add(parts.get(i));
        }

        return String.join(":", normalizedParts);
    }

    private static String normalizeRoot(String root) {
        String lowerRoot = root.trim().toLowerCase();

        if (!VALID_ROOTS.contains(lowerRoot)) {
            throw new IllegalArgumentException(
                    "Invalid account root: " + root
                            + ". Root must be one of Assets, Liabilities, Equity, Income, Expenses.");
        }

        switch (lowerRoot) {
        case "assets":
            return "Assets";
        case "liabilities":
            return "Liabilities";
        case "equity":
            return "Equity";
        case "income":
            return "Income";
        case "expenses":
            return "Expenses";
        default:
            throw new IllegalArgumentException("Invalid account root: " + root);
        }
    }

    @Override
    public String toString() {
        return fullName;
    }
}
