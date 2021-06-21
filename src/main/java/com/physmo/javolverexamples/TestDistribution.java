package com.physmo.javolverexamples;

// Idea: generate every configuration of DNA given size of dna, range of values and
// granularity of values.
// NOTE: After I coded this I realised the number of combinations quickly reaches an absolutely massive
// number that makes this idea totally infeasible ... shame!

public class TestDistribution {

    public static void main(String[] args) {
        //test1(974101, 8, 10);
        //buildTranslations(4,0,1);
        System.out.println("getNumberOfCombinations "+getNumberOfCombinations(10,3));
    }

    public static int getNumberOfCombinations(int numValues, int base) {
        int totalCombinations = 1;
        for (int i=0;i<numValues;i++) {
            totalCombinations*=base;
        }
        return totalCombinations;
    }

    // Build list of translation values to convert the raw digits into floating point ranged values.
    public static double[] buildTranslations(int base, double lowerRange, double upperRange) {
        double translations[] = new double[base];
        double span = (upperRange-lowerRange)/(double)(base-1);
        for (int i=0;i<base;i++) {
            translations[i]=lowerRange+(span*i);
        }
        return translations;
    }

    public static double[] createDnaForIndex(int inputValue, int numValues, int base) {

        int digits[] = new int[numValues];
        double translatedDigits[] = new double[numValues];
        double translations[] = buildTranslations(base,0,1);

        int rollingValue = inputValue;

        int totalCombinations = getNumberOfCombinations(numValues, base);

        for (int i=numValues-1;i>=0;i--) {
            int digitMagnitude = raiseToPower(base, i);
            int numOccurrences = rollingValue/digitMagnitude;
            rollingValue-=numOccurrences*digitMagnitude;
            digits[-1+digits.length-i]=numOccurrences;
            //System.out.println("i: "+i+" digitMagnitude:" + digitMagnitude + " numOccurences:"+numOccurrences+" rollingValue: "+ rollingValue);
        }

        //System.out.println("totalCombinations "+totalCombinations);

        String output="";
        for (int i = 0;i<numValues;i++) {
            output+=digits[i]+",";
        }
        //System.out.println(">> "+output);

        // Translate digits to double values.
        for (int i=0;i<numValues;i++) {
            translatedDigits[i] = translations[digits[i]];
        }

        return translatedDigits;
    }

    public static int raiseToPower(int base, int exponent) {
        //System.out.println("RTP base:"+base+" exponent:"+exponent+"  = "+(int)Math.pow(base,exponent));
        return (int)Math.pow(base,exponent);
    }

}
