public class BrailleBuilder {
    private int value;
    private int counter;
    Map<Integer, String> keyMap;

    public BrailleBuilder () {
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.values.braille-eng.csv)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                keyMap[Integer.parseInt(row[1])] = row[0]; 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        value = 0;
        counter = 0;
    }

    public boolean isReady() {
        return this.counter % 3 == 0 && this.getChar() != '\0';
    }

    public String getChar() {
        assert(this.counter % 3 == 0);
        return keyMap.get(value);  // null not found, '\0' need another block
    }

    public void reset() {
        value = 0;
        counter = 0;
    }

    public void input(int in) {
        assert(!this.isReady());
        value = 4 * value + in;
        counter++;
    }
}