import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Evolution {
    private HashMap<Integer, Integer> weights = new HashMap<>();
    private HashMap<Integer, Integer> prices = new HashMap<>();
    private HashMap<Integer, Backpack> parents = new HashMap<>();
    private HashMap<Integer, Backpack> children = new HashMap<>();
    private HashMap<Integer, Integer> take = new HashMap<>();
    private HashMap<Integer, Double> probability = new HashMap<>();
    private HashMap<Integer, Integer> quantity = new HashMap<>();
    private int bestchild = 0;
    private String bestchildstr = "";
    private int steps = 0;
    private int size = 0;
    private int populationsize = 0;
    private int cross = 0;
    private int form = 0;
    private int mutate = 0;
    private int select = 0;
    private int maxweight = 150;
    private int backpackweight = 0;
    private String path = "/home/just/IdeaProjects/EGA10/src/test";
    private File file = new File(path);
    Evolution (int steps, int populationsize,int form, int cross, int mutate, int select) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        this.steps = steps;
        this.populationsize = populationsize;
        this.form = form;
        this.mutate = mutate;
        this.select = select;
        this.cross = cross;
        String line = scanner.nextLine();
        String[] backpack = line.split(" ");
        this.size = Integer.parseInt(backpack[0]);
        int [] values = new int[3];
        int counter = 0;
        for (int i = 0; i < this.size; i++) {
            line = scanner.nextLine();
            backpack = line.split(" ");
            for (String value : backpack) {
                values[counter] = Integer.parseInt(value);
                counter++;
            }
            this.prices.put(i, values[1]);
            this.weights.put(i, values[2]);
            this.take.put(i,0);
            counter = 0;
        }
        line = scanner.nextLine();
        backpack = line.split(" ");
        this.maxweight = Integer.parseInt(backpack[0]);
        scanner.close();
    }
    public void form() {
        while (!itisok())
        {
            this.maxweight -=10;
        }
        if (form == 1) {
            randform();
        } else if (form == 2) {
            randform();
            for (int i = 0; i<this.populationsize;i++)
            {
                this.parents.get(i).max();
            }
        }
    }
    public void evolutionfunction()
    {
        printmatrix();
        form();
        System.out.println("Сформированное первое поколение: ");
        println();
        for (int i = 0; i<this.steps;i++)
        {
            crossover();
            mutation();
            selection();
            System.out.println("---------------");
            System.out.println("Шаг " + (i+1));
            System.out.println("---------------");
            Backpack childbuffer = new Backpack(this.maxweight, bestchildstr, this.prices, this.weights);
            parents.remove(0);
            parents.put(0,childbuffer);
            println();
            if (all())
            {
                System.out.println("Больше шагов не требуется");
                i = this.steps;
            }
        }
        System.out.println("Лучшее решение: "+ this.parents.get(bestchild).takestr
                + "   " + this.parents.get(bestchild).sum);
    }
    public void println()
    {
        int max = 0;
        for (int i = 0; i < this.populationsize; i++)
        {
            if (this.parents.get(i)!=null) {
                if (max < this.parents.get(i).println())
                {
                    this.bestchild = i;
                    max = this.parents.get(i).sum;
                }
                System.out.println();
            }
        }
        System.out.print("Лучшая особь в этом поколении - ");
        this.parents.get(this.bestchild).println();
        bestchildstr = this.parents.get(this.bestchild).takestr;
        System.out.println();
    }

    private void randform()
    {
        Random rnd = new Random();
        int buffer = 0;
        int index = 0;
        int square = 0;
        Backpack childbuffer = new Backpack(0,"100",this.prices,this.weights);
        String bufferstr = "";
        while (index<this.populationsize) {
            square = Math.abs(rnd.nextInt() % this.size);
            buffer = this.size * square + Math.abs(rnd.nextInt() % this.size);
            bufferstr = dectotwo(buffer, this.size);
            childbuffer = new Backpack(this.maxweight, bufferstr, this.prices, this.weights);
            if (childbuffer.itisnormal()) {
                parents.put(index, childbuffer);
                index++;
            }
        }
    }
    private void printmatrix()
    {
        for (int i = 0; i<this.size;i++)
        {
            System.out.print(this.weights.get(i) + "\t");
            System.out.println(this.prices.get(i));
        }
        System.out.println(this.maxweight);
    }
    private void crossover(){
        int min = 32000;
        int bestpartner = 0;
        for (int i = 0; i< this.populationsize; i++) {
            min = 32000;
            for (int j = 0; j < this.populationsize; j++) {
                if (take.get(i) != null && take.get(j) !=null) {
                    if (take.get(i) != 1) {
                        if (take.get(j) != 1 && min > hemmingdistance(i, j) && i != j) {
                            min = hemmingdistance(i, j);
                            bestpartner = j;
                        }
                    }
                }
            }

            if (min != 32000 && take.get(i) != 1) {
                take.put(i,1);
                take.put(bestpartner,1);
                if (this.cross == 1) {
                    this.multipoint(i, bestpartner);
                } else if (this.cross == 2) {
                    homogeneous(i, bestpartner);
                }
            }
        }
    }
    private void multipoint(int first, int second)
    {
        int index = this.children.size();
        int rand = 0;
        int buffer = 0;
        Backpack childbuffer = new Backpack(0,"100",this.prices,this.weights);
        String firstchild = "", secondchild = "";
        Random rnd = new Random();
        rand = Math.abs(rnd.nextInt()) % (this.size/2) + 1;
        HashMap <Integer,Integer> points = new HashMap<>();
        for (int i = 0; i<rand; i++)
        {
            points.put(i,this.size + 1);
        }
        for (int i = 0; i<rand; i++)
        {
            if (i == 0) {
                buffer = Math.abs(rnd.nextInt()) % (this.size / rand);
                points.put(i,buffer);
            }
            else
            {
                if (points.get(i-1) >= this.size-1) break;
                while (points.get(i) >= this.size)
                {
                    buffer = points.get(i-1) + Math.abs(rnd.nextInt()) % 3 + 1;
                    points.put(i,buffer);
                }
            }
        }
        for (int i = 0; i<rand; i++)
        {
            if (points.get(i) >= this.size)
            {
                points.remove(i);
            }
        }
        rand = points.size();
        for (int i = 0; i< rand; i++)
        {
            if (i == 0)
            {
                firstchild += parents.get(first).takestr.substring(0, points.get(i));
                secondchild += parents.get(second).takestr.substring(0, points.get(i));
            } else

                if (i % 2 == 0) {
                    firstchild += parents.get(first).takestr.substring(points.get(i-1), points.get(i));
                    secondchild += parents.get(second).takestr.substring(points.get(i-1), points.get(i));
                } else
                {
                    firstchild += parents.get(second).takestr.substring(points.get(i-1), points.get(i));
                    secondchild += parents.get(first).takestr.substring(points.get(i-1), points.get(i));
                }
        }
        if (rand % 2 == 0)
        {
            firstchild += parents.get(first).takestr.substring(points.get(rand-1), this.size);
            secondchild += parents.get(second).takestr.substring(points.get(rand-1), this.size);
        } else
        {
            firstchild += parents.get(second).takestr.substring(points.get(rand-1), this.size);
            secondchild += parents.get(first).takestr.substring(points.get(rand-1), this.size);
        }
        childbuffer = new Backpack(this.maxweight,firstchild,this.prices,this.weights);
        this.children.put(index,childbuffer);
        childbuffer = new Backpack(this.maxweight,secondchild,this.prices,this.weights);
        this.children.put(index+1,childbuffer);
    }
    private void homogeneous(int first, int second)
    {
        int index = this.children.size();
        int rand = 0;
        Backpack childbuffer = new Backpack(0,"100",this.prices,this.weights);
        String firstchild = "", secondchild = "";
        Random rnd = new Random();
        for (int i = 0; i< this.size; i++)
        {
            rand = Math.abs(rnd.nextInt()) % 2;
            if (rand == 0) firstchild += parents.get(first).takestr.charAt(i);
            else firstchild += parents.get(second).takestr.charAt(i);
            rand = Math.abs(rnd.nextInt()) % 2;
            if (rand == 0) secondchild += parents.get(first).takestr.charAt(i);
            else secondchild += parents.get(second).takestr.charAt(i);
        }
        childbuffer = new Backpack(this.maxweight,firstchild,this.prices,this.weights);
        this.children.put(index,childbuffer);
        childbuffer = new Backpack(this.maxweight,secondchild,this.prices,this.weights);
        this.children.put(index+1,childbuffer);
    }
    private void mutation(){
        Random rnd = new Random();
        int rand = 0;
        for (int i = 0; i< children.size(); i++) {
            rand = Math.abs(rnd.nextInt()) % 2;
            if (rand == 1)
            {
                if (this.mutate == 1)
                {
                    point(i);
                }
                else if (this.mutate == 2)
                {
                    addition(i);
                }
            }
        }
    }
    private void point(int index)
    {
        Random rnd = new Random();
        int rand = 0;
        String str = "";
        rand = Math.abs(rnd.nextInt()) % this.size;
        if (this.children.get(index)!=null)
           str = changeIndexString(this.children.get(index).takestr,rand);
        Backpack childbuffer = new Backpack(this.maxweight,
                str, this.prices, this.weights);
        this.children.put(index,childbuffer);
    }
    private void addition(int index)
    {
        String str = "";
        if (this.children.get(index)!=null) {
            str = this.children.get(index).takestr;
            for (int i = 0; i < this.size; i++) {
                str = changeIndexString(str, i);
            }
            Backpack childbuffer = new Backpack(this.maxweight,
                    str, this.prices, this.weights);
            this.children.put(index, childbuffer);
        }
    }
    private void selection(){
        if (this.select == 1)
        {
            proportional();
        }
        else if (this.select == 2)
        {
            linear();
        }
        universal();
        for (int i =0; i < this.populationsize; i++)
        {
            for (int j = 0; j<this.quantity.get(i);j++)
            {
                this.children.put(this.children.size(),this.parents.get(i));
            }
        }
        for (int i = 0; i < populationsize; i++)
        {
            take.put(i,0);
        }
        this.children.put(this.children.size(),this.parents.get(this.bestchild));
        int q = this.children.size();
        int index = 0;
        for (int i = 0; i < q; i++)
            if (children.get(i)!=null)
            if (!children.get(i).itisnormal()) children.remove(i);
            HashMap <Integer,Backpack> bufhash = new HashMap<>();
            for (int i = 0; i<q; i++)
                if (children.get(i)!=null)
                {
                    bufhash.put(index,children.get(i));
                    index++;
                }
            for (int i = 0; i<q; i++)
                if (children.get(i) != null) children.remove(i);
                for (int i = 0; i<bufhash.size();i++)
                {
                    children.put(i,bufhash.get(i));
                }
                if (children.size()>populationsize)
                {
                    for (int i = populationsize; i<children.size();i++)
                    {
                        children.remove(i);
                    }
                    for (int i = 0; i < populationsize; i++)
                    {
                        if (children.get(i) == null || children.get(i).sum == 0)
                        {
                            children.put(i,parents.get(getmax()));
                        }
                    }
                }
                else if (children.size()<populationsize)
                {
                    for (int i = children.size(); i<populationsize;i++)
                    {
                        children.put(i,parents.get(getmax()));
                    }
                    for (int i = 0; i < populationsize; i++)
                    {
                        if (children.get(i) == null || children.get(i).sum == 0)
                        {
                            children.put(i,parents.get(getmax()));
                        }
                    }
                }
                else {
                    for (int i = 0; i < populationsize; i++)
                    {
                        if (children.get(i) == null || children.get(i).sum == 0)
                        {
                            children.put(i,parents.get(getmax()));
                        }
                    }
                }
                for (int i = 0; i< populationsize; i++)
                {
                    if (children.get(i).sum < parents.get(bestchild).sum)
                    {
                        children.put(i,parents.get(bestchild));
                        i+=populationsize;
                    }
                }
                for (int i = 0; i<populationsize;i++)
                {
                    if (i != bestchild)
                    parents.put(i,children.get(i));
                }

    }
    private void universal(){
        double buf = 0.0;
        Backpack backpackbufi;// = new Backpack(this.maxweight, "010", this.prices, this.weights);
        Backpack backpackbufj;// = new Backpack(this.maxweight, "010", this.prices, this.weights);
        for (int i = 0; i < this.populationsize; i++)
        {
            for (int j = 0; j< this.populationsize; j++)
                if (this.parents.get(i).sum<this.parents.get(j).sum)
                {
                    backpackbufi = new Backpack(this.maxweight, this.parents.get(i).takestr, this.prices, this.weights);
                    backpackbufj = new Backpack(this.maxweight, this.parents.get(j).takestr, this.prices, this.weights);
                    this.parents.put(i,backpackbufj);
                    this.parents.put(j,backpackbufi);
                    buf = this.probability.get(i);
                    this.probability.put(i,this.probability.get(j));
                    this.probability.put(j,buf);
                }
        }
        for (int i = 0; i<this.populationsize; i++)
        {
            this.quantity.put(i,0);
        }
        Random rnd = new Random();
        HashMap<Integer,Integer> sigma = new HashMap<>();
        for (int i= 0; i<this.populationsize;i++) {
            sigma.put(i, (Math.abs(rnd.nextInt()) % (sum(this.populationsize) / populationsize))
                    + (sum(this.populationsize) / populationsize) * i);
        }
        for (int i = 0; i< this.populationsize; i++)
        {
         for (int j = 0; j < this.populationsize; j++)
         {
             if (sigma.get(j)>sumi(j,i) && sigma.get(j) < sumi(j+1,i))
                 this.quantity.put(i,this.quantity.get(i) + 1);
         }
        }
        double bufdouble = 0.0;
        for (int i = 0; i<this.populationsize; i++) {
            bufdouble = this.quantity.get(i) * this.probability.get(i);
            this.quantity.put(i, (int) Math.round(bufdouble));
        }
    }
    private void proportional(){
        int sumbuf = 0;
        for (int i = 0; i<this.populationsize;i++)
        {
            sumbuf+=this.parents.get(i).sum;
        }
        if (superindex()==-1)
        {
            for (int i = 0; i< this.populationsize;i++)
            {
                this.probability.put(i, (double) (this.parents.get(i).sum / sumbuf));
            }
        } else
        {
            for (int i = 0; i<this.populationsize;i++)
            {
                this.probability.put(i, (double) (superindex()/this.populationsize));
            }
            this.probability.put(this.bestchild,1.0);
        }
    }
    private void linear(){
        Backpack backpackbufi;// = new Backpack(this.maxweight, "010", this.prices, this.weights);
        Backpack backpackbufj;// = new Backpack(this.maxweight, "010", this.prices, this.weights);
        for (int i = 0; i < this.populationsize; i++)
        {
            for (int j = 0; j< this.populationsize; j++)
                if (this.parents.get(i).sum<this.parents.get(j).sum)
                {
                    backpackbufi = new Backpack(this.maxweight, this.parents.get(i).takestr, this.prices, this.weights);
                    backpackbufj = new Backpack(this.maxweight, this.parents.get(j).takestr, this.prices, this.weights);
                    this.parents.put(i,backpackbufj);
                    this.parents.put(j,backpackbufi);
                }
        }
        int nfirst = 1, nlast = this.populationsize;
        this.probability.put(0, (double) (nfirst/this.populationsize));
        double buffer = 0.0;
        for (int i = 1; i < this.populationsize -1; i++)
        {
            buffer = nfirst + (nlast - nfirst)*((double) i/(this.populationsize-1));
            this.probability.put(i,buffer);
        }
        this.probability.put(this.populationsize-1, (double) (nlast/this.populationsize));
    }
    private int hemmingdistance(int first, int second)
    {
        int distance = 0;
        for (int i = 0; i < this.size; i++)
        {
           if (this.parents.get(first)
                   .takestr.charAt(i) != this.parents.get(second)
                   .takestr.charAt(i)) distance++;
        }
        return distance;
    }
    public String changeIndexString(String s, int pos)
    {
        String s1 = "";
        try
        {
            s.charAt(pos);
        } catch (Exception e)
        {
            //System.out.println("Вышли за границу HashMap");
            pos = -1;
        }
        if (pos>=0) {
            for (int i = 0; i < pos; i++)
                s1 += s.charAt(i);
            if (s.charAt(pos) == '1') s1 += '0';
            else s1 += '1';
            if (pos < s.length() - 1)
                s1 += s.substring(pos + 1);
        }
        return s1;
    }
    private int superindex() {
    int index = -1;
    for (int i = 0; i<this.populationsize; i++)
    {
        if (this.parents.get(this.bestchild).sum > this.parents.get(i).sum * 2) {
            if (this.parents.get(i).sum == 0)
            index = this.parents.get(this.bestchild).sum;
            else
                index = this.parents.get(this.bestchild).sum/ this.parents.get(i).sum;
        }
    }
    return index;
    }
    private int sum(int q)
    {
        int bufsum = 0;
        for (int i = 0; i<q; i++)
        {
            bufsum +=i;
        }
        return bufsum;
    }
    private int sumi(int q, int index)
    {
        int bufsum = 0;
        for (int i = 0; i<q; i++)
        {
            bufsum +=index;
        }
        return bufsum;
    }
    private int getmax()
    {
        int max=0;
        int maxindex = 0;
        for (int i = 0; i<this.populationsize;i++)
            if (max < this.parents.get(i).sum && this.take.get(i)!=1)
            {
                max = this.parents.get(i).sum;
                maxindex = i;
            }
        take.put(maxindex,1);
            return maxindex;
    }
    public  int mypow(int first, int second)
    {
        int buffer = first;
        if (second == 0) return 1;
        else {
            for (int i = 0; i < second - 1; i++) {
                first = first * buffer;
            }
            return first;
        }
    }
    public  String dectotwo(int dec,int num)
    {
        String result = "";
        while (dec > 0)
        {
            result+=String.valueOf(dec % 2);
            dec /=2;
        }
        while (result.length()<this.size)
        {
            result += "0";
        }
        String buffer = "";
        for (int i = 0; i<result.length(); i++)
            buffer += result.charAt(result.length() - i - 1);
        return buffer;
    }
    private boolean all()
    {
     boolean flag = true;
     String buffer = parents.get(0).takestr;
     for (int i = 0; i<populationsize; i++)
     {
         if (!buffer.equals(parents.get(i).takestr))
             flag = false;
     }
     return flag;
 }
    private boolean itisok()
    {
        boolean itisok = true;
        int sum = 0;
        for (int i = 0; i<this.size; i++)
        {
            sum += weights.get(i);
        }
        if (sum <= this.maxweight)
            itisok = false;
        return  itisok;
    }
}
