package senclassify.classify;

/**
 * Created by zhangzhiyong on 17-4-6.
 */

public class LogisticRegresion {
    //the weight of each variables of the model
    public static double n_Pars[];

    //the iteration of the calculation
    public int MAXITER = 1000;

    //the step of the gratitude
    public double STEP = 1e-2;

    public void train(double[][] X, double[] Y){
        //计算样本个数以及每个样本的维度
        int size = X.length;
        int dim = X[0].length;

        //这个是初始化的权重
        n_Pars = new double[dim];
        for(int i = 0; i < dim; i ++){
            n_Pars[i] = 1.0;
        }

        //采用的算法是批量梯度下降算法
        for(int iter = 0; iter < MAXITER; iter ++){
            //out存放着输出的结果，主要目的是未了和标准结果对比
            double[] out = new double[size];

            for(int s = 0; s < size; s++){
                double temp = innerProduct(n_Pars, X[s]);
                out[s] = sigmoid(temp);
            }

            //计算误差，并且更新权重
            for(int d = 0; d < dim; d ++){
                double sum = 0;
                for(int s = 0; s < size; s ++){
                    sum += (Y[s] - out[s]) * X[s][d];
                }
                //临时变量的保存
                double temp_Para = n_Pars[d]
                n_Pars[d] = temp_Para + STEP * sum;
             //   n_Pars[d] = (double) (temp_Para + STEP * sum - 0.01 * Math.pow(temp_Para, 2));  L2正则
             //  n_Pars[d] = (double) (temp_Para + STEP * sum - 0.01 * Math.abs(temp_Para));  L1正则
            }
        }
    }
   //返回的两个向量的内积
    private double innerProduct(double[] w, double[] sample){
        double result = 0.0;
        for(int i = 0; i < w.length; i ++)
            result += w[i] * sample[i];
        return result;
    }
    //回返sigmoid值
    private double sigmoid(double z){
        return 1 / (1 + Math.exp(- z));
    }
    
    //对样本进行预测
    public double predicted(double[] sample){
        if(sample.length != n_Pars.length){
            System.out.println("error existed !!!");
            return -1;
        }

        double cur = innerProduct(n_Pars, sample);
        double label = sigmoid(cur);
        if(label >= 0.5)
            return 1;
        else
            return 0;
    }

    public static void main(String[] args){
        LogisticRegresion lr = new LogisticRegresion();
        double[][] x = {{1.0, 2.0, 3.0, 4.5}, {2.0, 4.0, 6.0, 5.2}, {3.0, 6.0, 9.0, 6.1}};
        double[] y = {1.0, 1.0, 1.0, 0.0};

        double[] test = {2.0, 3.0, 4.5,3.2};
        lr.train(x, y);
        double value = lr.predicted(test);
        for(int i = 0; i < lr.n_Pars.length; i ++)
            System.out.print(n_Pars[i] + " ");
        System.out.println(value);
    }
}
