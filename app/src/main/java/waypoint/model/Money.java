package waypoint.model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/** Exact, currency-aware, immutable money measured in minor units. */
public final class Money implements Comparable<Money> {
    public static final Currency INR = Currency.getInstance("INR");
    public static final Currency USD = Currency.getInstance("USD");

    private final long minor;
    private final Currency currency;

    private Money(long minor, Currency currency) {
        this.minor = minor;
        this.currency = Objects.requireNonNull(currency, "currency");
    }

    public static Money ofMinor(long minor, Currency currency) {
        return new Money(minor, Objects.requireNonNull(currency, "currency"));
    }

    public static Money rupees(long rupees) {
        return ofMinor(Math.multiplyExact(rupees, 100L), INR);
    }

    public long minor() {
        return minor;
    }

    public Currency currency() {
        return currency;
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return ofMinor(Math.addExact(minor, other.minor), currency);
    }

    public Money minus(Money other) {
        requireSameCurrency(other);
        return ofMinor(Math.subtractExact(minor, other.minor), currency);
    }

    public Money times(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("multiplier must not be negative");
        }
        return ofMinor(Math.multiplyExact(minor, (long) multiplier), currency);
    }

    public boolean isNegative() {
        return minor < 0;
    }

    public List<Money> allocate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("number of shares must be positive");
        }
        if (isNegative()) {
            throw new IllegalArgumentException("cannot allocate negative money");
        }

        long quotient = minor / n;
        long remainder = minor % n;
        List<Money> shares = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            shares.add(ofMinor(quotient + (i < remainder ? 1 : 0), currency));
        }
        return List.copyOf(shares);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other");
        if (!currency.equals(other.currency)) {
            throw new CurrencyMismatch("cannot mix " + currency + " and " + other.currency);
        }
    }

    @Override
    public int compareTo(Money other) {
        Objects.requireNonNull(other, "other");
        requireSameCurrency(other);
        return Long.compare(minor, other.minor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money money)) {
            return false;
        }
        return minor == money.minor && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minor, currency);
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " + (minor / 100) + "." + String.format("%02d", minor % 100);
    }
}

class CurrencyMismatch extends RuntimeException {
    public CurrencyMismatch(String message) {
        super(message);
    }
}
