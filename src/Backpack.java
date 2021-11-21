import java.util.HashMap;

public class Backpack {
    private HashMap<Integer,Integer> prices = new HashMap<>();
    private HashMap<Integer,Integer> take = new HashMap<>();
    private HashMap<Integer,Integer> weights = new HashMap<>();
    public String takestr = "";
    private int maxweight = 0;
    public int sum = 0, sumweight = 0, size =0;
    Backpack (int maxweight, String takes, HashMap<Integer,Integer> prices,
              HashMap<Integer, Integer> weights)
    {
        this.takestr = takes;
        this.size = takes.length();
        for (int i = 0; i < this.size; i++)
        {
            if (takes.charAt(i) == '1')
            {
                this.take.put(i,1);
            } else this.take.put(i,0);
        }
        this.maxweight = maxweight;
        for (int i = 0; i < this.size; i++)
        {
            this.prices.put(i,prices.get(i));
            this.weights.put(i,weights.get(i));
        }
        for (int i = 0; i<this.size; i++)
            if (take.get(i) == 1) {
                sumweight += this.weights.get(i);
                sum += this.prices.get(i);
            }
    }
    public boolean itisnormal()
    {
        if (this.sumweight>this.maxweight) return false;
        else return true;
    }
    public void max()
    {
        String buffer = "";
        int max = 0, maxindex = 0;
        for (int j = 0; j < this.size; j++) {
            for (int i = 0; i < this.size; i++) {
                if (take.get(i) != 1)
                    if (prices.get(i) > max) {
                        max = prices.get(i);
                        maxindex = i;
                    }
            }
            if (weights.get(maxindex) + this.sumweight <= this.maxweight
                    && !end()) {
                this.take.put(maxindex, 1);
                buffer = this.takestr.substring(maxindex+1,this.size);
                this.takestr = this.takestr.substring(0,maxindex);
                this.takestr = this.takestr + "1" + buffer;
                this.sumweight += weights.get(maxindex);
                this.sum += prices.get(maxindex);
            }
            max = 0;
        }
    }
    public int println()
    {
        for (int i = 0; i< this.size; i++)
        {
            System.out.print(take.get(i));
            /*if (take.get(i) == 1)
            {
                System.out.print(i + " ");
            }*/
        }
        System.out.print(": " + this.sum);
        System.out.print(", " + this.sumweight);
        return this.sum;
    }
    public boolean end()
    {
        boolean flag = true;
        for (int i = 0; i<this.size; i++)
        {
            if (take.get(i) == 0) flag = false;
        }
        return flag;
    }
}

