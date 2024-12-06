public class Gap_Buffer {
    private int buffer_len = 512; // 512 base?
    private int buffer_size = buffer_len;
    public int cursor_index = 0;
    private char[] string_arr = new char[buffer_len]; // It's size is equal to gap len

    public enum Direction { LEFT, RIGHT }

    Gap_Buffer() {}

    Gap_Buffer(int buffer_len) {
        this.buffer_len = buffer_len;
        this.buffer_size = buffer_len;
        this.string_arr = new char[buffer_len];
    }

    public void add_character (char c) {
        // Add char, adjust window, decrease buffer size
        string_arr[cursor_index] = c;
        cursor_index++;
        buffer_size--;

        if (buffer_size <= 0) {
            resize();
        }
    }

    public void delete_character(char c) {
        if (cursor_index <= 0) {
            throw new IndexOutOfBoundsException("Cannot delete character: no valid index to delete.");
        }

        // Delete char, adjust window, increase buffer size
        string_arr[cursor_index] = '\u0000'; // Empty char ?, I think this is what a empty char is initialized to
        cursor_index--;
        buffer_size++;
        return;
    }

    public void move_cursor(Direction dir) {
        switch (dir) {
            case LEFT:
                if (cursor_index <= 0) {
                    throw new IndexOutOfBoundsException("Cannot move cursor: Cursor is all the way at the left");
                }

                // char before buffer_index get moved to end of buffer
                string_arr[cursor_index + buffer_size - 1] = string_arr[cursor_index - 1];

                // move cursor back
                cursor_index--;
                string_arr[cursor_index] = '\u0000';


                break;

            case RIGHT:
                if (cursor_index >= string_arr.length - buffer_size) {
                    throw new IndexOutOfBoundsException("Cannot move cursor: Cursor is all the way at the right");
                }

                string_arr[cursor_index] = string_arr[cursor_index + buffer_size];

                // move cursor forward
                cursor_index++;
                string_arr[cursor_index + buffer_size - 1] = '\u0000';
                break;
        }
        return;
    }

    private void resize() {
        if (buffer_size > 0) { throw new IllegalArgumentException("Buffer size should never be positive."); }

        int old_buffer_len = buffer_len;
        int new_buffer_len = buffer_len * 2;
        char[] new_string_arr = new char[old_buffer_len + new_buffer_len];

        // Copy all chars before cursor_index
        System.arraycopy(string_arr, 0, new_string_arr, 0, cursor_index);

        // Make new empty buffer at cursor index
        for (int i = cursor_index; i < cursor_index + new_buffer_len; i++) {
            new_string_arr[i] = '\u0000';
        }

        // Make new empty buffer at cursor index
        System.arraycopy(string_arr, cursor_index, new_string_arr, cursor_index + new_buffer_len, old_buffer_len - cursor_index);

        // Update properties
        buffer_len = new_buffer_len;
        buffer_size = new_buffer_len;
        string_arr = new_string_arr;
    }

    @Override
    public String toString() {
        StringBuilder ret_val = new StringBuilder();
        for (char c : string_arr) {
//            if (c == '\u0000') { continue; }
            ret_val.append(c);
        }

        return ret_val.toString();
    }
}
