package maowcraft.collectorsitems.util;

public enum RequiredAmount {
    SINGLE_STACK(36),
    SIXTEEN_STACK(576),
    SIXTY_FOUR_STACK(500);

    public int amount;

    RequiredAmount(int amount) {
        this.amount = amount;
    }
}
