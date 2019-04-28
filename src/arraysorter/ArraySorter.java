package arraysorter;

import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArraySorter<E extends Comparable> extends Thread
{
    public Integer[] list;
    public Random rand = new Random();
    public boolean running = false;
    public String sortMethod = "";
    
    public ArraySorter(int numElements)
    {
        randList(numElements);
    }   
    
    public void randList(int numElements)
    {
        list = new Integer[numElements];
        for(int i = 0; i < numElements; i ++)
        {
            list[i] = rand.nextInt(600);
        }
    }
    
    @Override
    public void run()
    {
        while(running)
        {
            try 
            {
                switch(sortMethod)
                {
                    case("Merge Sort"):
                        this.mergeSort((E[]) list);
                        break;
                    case("Quick Sort"):
                        this.quickSort((E[]) list);
                        break;
                    case("Insertion Sort"):
                        this.insertionSort((E[]) list);
                        break;
                    case("Shell Sort"):
                        this.ShellSort(list);
                        break;
                    case("Cocktail Sort"):
                        this.cocktailSort((E[]) list);
                        break;
                    case("Bubble Sort"):
                        this.bubbleSort((E[]) list);
                        break;
                    case("Selection Sort"):
                        this.selectionSort((E[]) list);
                        break;
                    case("Bogo Sort"):
                        list = this.BogoSort(list);
                        break;
                }
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(ArraySorter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Integer[] ShellSort(Integer[] list) throws InterruptedException
    {
        // setup index size = 15, gap = 7, pass = 1
        int gap = list.length / 2;
        int pass = 1;
        int index = 0;
        int temp = 0;

        int smallerIndex = 0;
        int largerIndex = 0;
        boolean swapped = false;


        // this is how many passes are needed, this is generally (size / 2 - 1)
        while (gap >= 1)
        {
            // while (index + gap) is still in range of the total size of the input array
            while (index + gap < list.length)
            {
                int firstNumber = list[index];
                int secondNumber = list[index + gap];

                // swap the index value with the gap value
                if (firstNumber > secondNumber)
                {
                    temp = list[index];
                    list[index] = list[index + gap];
                    list[index + gap] = temp;

                    // once swapped, we need to look at the index gaps before the current index also, then swap these if index is less than the new gap
                    swapped = true;
                    largerIndex = index;
                    smallerIndex = index - gap;
                    
                    this.sleep(20);
                    
                    // if the the current index is swapped with the gap then also check all the indexes before if there are some
                    // if the index - gap is still within the array (>0) then we need to check those values also
                    // if the value was swapped, then keep checking down the line
                    while (smallerIndex >= 0 && swapped == true)
                    {
                        // check to see if these numbers need to be swapped
                        if (list[smallerIndex] > list[largerIndex])
                        {
                            temp = list[smallerIndex];
                            list[smallerIndex] = list[largerIndex];
                            list[largerIndex] = temp;
                            swapped = true;
                            this.sleep(20);
                        }

                        // the values did not need to be swapped, therefore we are done with this while loop
                        else
                        {
                            swapped = false;
                            continue;
                        }

                        //give the i2
                        largerIndex = smallerIndex;
                        smallerIndex -= gap;
                    }
                }

                index++;
            }

            // increase the number of passes by one | decrease the gap by half | reset the index to 0
            if (gap != 1)
            {
                index = 0;
                gap /= 2;
                pass++;
            }
            else
            {
                gap = 0;
            }
        }

        return list;
    }

    public Integer[] BogoSort(Integer[] input) throws InterruptedException
    {
        boolean sorted = true;
        int numberTried = 0;
        Integer[] permentation = new Integer[input.length];

        do
        {
            permentation = GenerateBogoSortPermutations(input);
            sorted = true;
            for (int i = 0; i < (permentation.length - 1); i++)
            {
                int current = permentation[i];
                int next = permentation[i + 1];

                if (current > next)
                {
                    sorted = false;
                    break;
                }
            }

            numberTried++;
            this.list = permentation;
            this.sleep(2);
            
        } while (!sorted);
        
        running = false;
        return permentation;
    }

    private static Integer[] GenerateBogoSortPermutations(Integer[] input)
    {
        Random rand = new Random();
        HashSet<Integer> set = new HashSet<Integer>();
        
        int length = input.length;
        int random = rand.nextInt(length);
        Integer[] output = new Integer[length];

        for (int i = 0; i < length; i++)
        {
            while (output[random] == null)
            {
                output[random] = input[i];
                set.add(random);
            }

            do {
                random = rand.nextInt(length);

                if (set.size() == length)
                {
                    break;
                }

            } while (set.contains(random));
        }

        return output;
    }
    
    /* The asymptotic complexity for a cocktail sort is O(n^2) as the worst case
    *  we will have to perform two loops each iterating over the list. Best case is when
    *  The list is almost sorted which would be a O(2n) algorithm. 
    *
    *  Some ways to make this algorithm better is to include flags that break out of the loop(s)
    *  when no swaps have been performed, meaning the list is sorted.
    */
    public void cocktailSort(E[] list) throws InterruptedException
    {
        // flag to break the loops if no swaps occured
        boolean unsorted = true;
        
        // index of the start and end of what we want to compare
        int start = 0;
        int end = list.length - 1;
        
        // while an element was swapped in the last loop, keep sorting as there are still elements out of order
        do
        {
            // re-sets the flag to false, this will break the loop if no swaps are performed
            unsorted = false;
            
            // loops thorugh the list, bubbling up the highest element
            for(int i = start; i < end; i ++)
            {
                // if i is greater than i + 1 then we need to swap the elements
                if(list[i].compareTo(list[i + 1]) > 0)
                {
                    E temp = list[i+1];
                    list[i+1] = list[i];
                    list[i] = temp;
                    unsorted = true;
                }
                
                this.sleep(2);
            }
            
            // no swaps were performed, the list is sorted already
            if(!unsorted){
                break;
            }
            
            // reset the swapped flag and decrement the end integer as we have already put the last element in place
            unsorted = false;
            end--;
            
           // loops thorugh the list, bubbling down the lowest element
            for(int i = end; i > start; i--)
            {
                // if i is greater than i + 1 then we need to swap the elements
                if(list[i].compareTo(list[i-1]) < 0)
                {
                    E temp = list[i-1];
                    list[i-1] = list[i];
                    list[i] = temp;
                    unsorted = true;
                }
                
                this.sleep(2);
            }
            
            // increment the start position as the lowest element is in the correct position now
            start++;
            
        } while(unsorted);
    }
   
   public void selectionSort(E[] list) throws InterruptedException
   {  
        int indexMin; // index of least element
        E temp; // temporary reference to an element for swapping
        
        for (int i=0; i < list.length - 1; i++)
        {   
            // find the least element that has index>=i
            indexMin = i;
            for (int j=i+1; j<list.length; j++)
            {  
                if (list[j].compareTo(list[indexMin])<0)
                  indexMin = j;
            }
            // swap the element at indexMin with the element at i
            temp = list[indexMin];
            list[indexMin] = list[i];
            list[i] = temp;
            this.sleep(100);
      }
   }
   
    public void insertionSort(E[] list) throws InterruptedException
    {  
        E elementInsert;
        for (int i=1; i<list.length; i++)
        {  
            // get the element at index i to insert at some index<=i
            elementInsert = list[i];
            
            // find index where to insert element to maintain 0..i sorted
            int indexInsert = i;
            while (indexInsert>0 && list[indexInsert-1].compareTo(elementInsert)>0)
            {  
                // shift element at insertIndex-1 along one to make space
                list[indexInsert] = list[indexInsert-1];
                indexInsert--;
                this.sleep(5);
            }
            
            // insert the element
            list[indexInsert] = elementInsert;
       }
    }
   
   public void bubbleSort(E[] list) throws InterruptedException
   {  E temp; // temporary reference to an element for swapping
      for (int i=list.length-1; i>=0; i--)
      {  // pass through indices 0..i and bubble (swap) adjacent
         // elements if out of order
         for (int j=0; j<i; j++)
         {  if (list[j].compareTo(list[j+1])>0)
            {  // swap the elements at indices j and j+1
               temp = list[j+1];
               list[j+1] = list[j];
               list[j] = temp;
            }
            this.sleep(2);
         } 
      }
   }
   
   public void quickSort(E[] list) throws InterruptedException
   {  quickSortSegment(list, 0, list.length);
   }
   
   // recursive method which applies quick sort to the portion
   // of the array between start (inclusive) and end (exclusive)
   private void quickSortSegment(E[] list, int start, int end) throws InterruptedException
    {  
        if (end-start>1) // then more than one element to sort
        {  
            // partition the segment into two segments
            int indexPartition = partition(list, start, end);
            
            // sort the segment to the left of the partition element
            quickSortSegment(list, start, indexPartition);
            
            // sort the segment to the right of the partition element
            quickSortSegment(list, indexPartition+1, end);
        }
    }
   
    // use the index start to partition the segment of the list
    // with the element at start as the partition element
    // separating the list segment into two parts, one less than
    // the partition, the other greater than the partition
    // returns the index where the partition element ends up
    private int partition(E[] list, int start, int end) throws InterruptedException
    {  
        E temp; // temporary reference to an element for swapping
        E partitionElement = list[start];
        
        int leftIndex = start; // start at the left end
        int rightIndex = end-1; // start at the right end
        
        // swap elements so elements at left part are less than
        // partition element and at right part are greater
        while (leftIndex<rightIndex)
        {  
            // find element starting from left greater than partition
            while (list[leftIndex].compareTo(partitionElement) <= 0 && leftIndex<rightIndex)
               leftIndex++; // this index is on correct side of partition
            
            // find element starting from right less than partition
            while (list[rightIndex].compareTo(partitionElement) > 0)
               rightIndex--; // this index is on correct side of partition
            
            if (leftIndex<rightIndex)
            {  // swap these two elements
               temp = list[leftIndex];
               list[leftIndex] = list[rightIndex];
               list[rightIndex] = temp;

            }
            
            this.sleep(100);
        }
        
        // put the partition element between the two parts at rightIndex
        list[start] = list[rightIndex];
        list[rightIndex] = partitionElement;
        return rightIndex;
    }
   
    public void mergeSort(E[] list) throws InterruptedException
    {  
        mergeSortSegment(list, 0, list.length);
    }

   // recursive method which applies merge sort to the portion
   // of the array between start (inclusive) and end (exclusive)
   private void mergeSortSegment(E[] list, int start, int end) throws InterruptedException
   {  int numElements = end-start;
      if (numElements>1)
      {  int middle = (start+end)/2;
         // sort the part to the left of middle
         mergeSortSegment(list, start, middle);
         // sort the part to the right of middle
         mergeSortSegment(list, middle, end);
         // copy the two parts elements into a temporary array
         E[] tempList = (E[])(new Comparable[numElements]); //unchecked
         for (int i=0; i<numElements; i++)
            tempList[i] = list[start+i];
         // merge the two sorted parts from tempList back into list
         int indexLeft = 0; // current index of left part
         int indexRight = middle-start; // current index of right part
         for (int i=0; i<numElements; i++)
         {  // determine which element to next put in list
            if (indexLeft<(middle-start))//left part still has elements
            {  if (indexRight<(end-start))// right part also has elem
               {  if (tempList[indexLeft].compareTo
                     (tempList[indexRight])<0) // left element smaller 
                     list[start+i] = tempList[indexLeft++];
                  else // right element smaller
                     list[start+i] = tempList[indexRight++];
               }
               else // take element from left part
                  list[start+i] = tempList[indexLeft++];
            }
            else // take element from right part
               list[start+i] = tempList[indexRight++];
            this.sleep(10);
         } 
      }
   }
}