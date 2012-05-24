package sortpom.wrapper;

import org.jdom.Content;
import org.jdom.Element;

/**
 * A wrapper that lets is element be unsorted
 *
 * @param <T>
 * @author Bjorn
 */
public class UnsortedWrapper<T extends Content> implements Wrapper<T> {

    /** The wrapped dom content. */
    private final T content;

    /**
     * Instantiates a new unsorted wrapper.
     *
     * @param content the content
     */
    public UnsortedWrapper(final T content) {
        this.content = content;
    }

    /** @see sortpom.wrapper.Wrapper#getContent() */
    @Override
    public T getContent() {
        return content;
    }

    /** @see sortpom.wrapper.Wrapper#isBefore(sortpom.wrapper.Wrapper) */
    @Override
    public boolean isBefore(final Wrapper<? extends Content> wrapper) {
        return false;
    }

    /** @see sortpom.wrapper.Wrapper#isContentElement() */
    @Override
    public boolean isContentElement() {
        return content instanceof Element;
    }

    /** @see sortpom.wrapper.Wrapper#isResortable() */
    @Override
    public boolean isResortable() {
        return false;
    }

    @Override
    public String toString() {
        return "UnsortedWrapper{" +
                "content=" + content +
                '}';
    }

    @Override
    public String toString(String indent) {
        return indent + toString();
    }
}
