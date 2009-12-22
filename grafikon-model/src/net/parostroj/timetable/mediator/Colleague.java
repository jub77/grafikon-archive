package net.parostroj.timetable.mediator;

/**
 * Colleague for mediator.
 *
 * @author jub
 */
public abstract class Colleague {

    private Mediator mediator;

    public Colleague() {
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void sendMessage(Object message) {
        mediator.sendMessage(message);
    }

    public abstract void receiveMessage(Object message);
}
