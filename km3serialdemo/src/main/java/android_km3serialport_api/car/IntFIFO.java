package android_km3serialport_api.car;

/**
 * int先进先出队列
 */
public class IntFIFO {
    private int[] queue;
    private int capacity;
    private int size;
    private int head;
    private int tail;

    public IntFIFO(int cap) {
        capacity = ( cap > 0 ) ? cap : 1; // at least 1
        queue = new int[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized boolean isEmpty() {
        return ( size == 0 );
    }

    public synchronized boolean isFull() {
        return ( size == capacity );
    }

    public synchronized void add(int b)
            throws InterruptedException {

        waitWhileFull();

        queue[head] = b;
        head = ( head + 1 ) % capacity;
        size++;

        notifyAll(); // let any waiting threads know about change
    }

    public synchronized void add(int[] list)
            throws InterruptedException {

        // For efficiency, the ints are copied in blocks
        // instead of one at a time. As space becomes available,
        // more ints are copied until all of them have been
        // added.
        if(list==null){return;}

        int ptr = 0;

        while ( ptr < list.length ) {
            // If full, the lock will be released to allow
            // another thread to come in and remove ints.
            waitWhileFull();

            int space = capacity - size;
            int distToEnd = capacity - head;
            int blockLen = Math.min(space, distToEnd);

            int intsRemaining = list.length - ptr;
            int copyLen = Math.min(blockLen, intsRemaining);

            System.arraycopy(list, ptr, queue, head, copyLen);
            head = ( head + copyLen ) % capacity;
            size += copyLen;
            ptr += copyLen;

            // Keep the lock, but let any waiting threads
            // know that something has changed.
            notifyAll();
        }
    }

    public synchronized int remove()
            throws InterruptedException {

        waitWhileEmpty();

        int b = queue[tail];
        tail = ( tail + 1 ) % capacity;
        size--;
        notifyAll(); // let any waiting threads know about change
        return b;
    }

    public synchronized int[] remove(int start,int removeLength)
            throws InterruptedException{
        if(start+removeLength>size){
            return removeAll();
        }
        int[] removed=new int[removeLength];
        for(int i=start;i<start+removeLength;i++){
            removed[i-start]=remove();
        }
        return removed;
    }

    public synchronized int[] removeAll() {
        // For efficiency, the ints are copied in blocks
        // instead of one at a time.

        if ( isEmpty() ) {
            // Nothing to remove, return a zero-length
            // array and do not bother with notification
            // since nothing was removed.
            return new int[0];
        }

        // based on the current size
        int[] list = new int[size];

        // copy in the block from tail to the end
        int distToEnd = capacity - tail;
        int copyLen = Math.min(size, distToEnd);
        System.arraycopy(queue, tail, list, 0, copyLen);

        // If data wraps around, copy the remaining data
        // from the front of the array.
        if ( size > copyLen ) {
            System.arraycopy(
                    queue, 0, list, copyLen, size - copyLen);
        }

        tail = ( tail + size ) % capacity;
        size = 0; // everything has been removed

        // Signal any and all waiting threads that
        // something has changed.
        notifyAll();

        return list;
    }

    public synchronized int[] removeAtLeastOne()
            throws InterruptedException {

        waitWhileEmpty(); // wait for a least one to be in FIFO
        return removeAll();
    }

    public synchronized int getFirstItemIndex(int item){
        int index=-1;
        for(int i=0;i<size;i++){
            if(queue[i]==item){
                index=i;
                break;
            }
        }
        return index;
    }

    public synchronized boolean waitUntilEmpty(long msTimeout)
            throws InterruptedException {

        if ( msTimeout == 0L ) {
            waitUntilEmpty();  // use other method
            return true;
        }

        // wait only for the specified amount of time
        long endTime = System.currentTimeMillis() + msTimeout;
        long msRemaining = msTimeout;

        while ( !isEmpty() && ( msRemaining > 0L ) ) {
            wait(msRemaining);
            msRemaining = endTime - System.currentTimeMillis();
        }

        // May have timed out, or may have met condition,
        // calc return value.
        return isEmpty();
    }

    public synchronized void waitUntilEmpty()
            throws InterruptedException {

        while ( !isEmpty() ) {
            wait();
        }
    }

    public synchronized void waitWhileEmpty()
            throws InterruptedException {

        while ( isEmpty() ) {
            wait();
        }
    }

    public synchronized void waitUntilFull()
            throws InterruptedException {

        while ( !isFull() ) {
            wait();
        }
    }

    public synchronized void waitWhileFull()
            throws InterruptedException {

        while ( isFull() ) {
            wait();
        }
    }
}
