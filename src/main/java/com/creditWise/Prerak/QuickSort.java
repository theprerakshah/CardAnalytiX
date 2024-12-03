package com.creditWise.Prerak;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QuickSort
{

	static int partition(List<Map.Entry<String, Integer>> list, int low, int high)
	{
		int pivot = list.get(high).getValue();

		int i = low - 1;

		for(int j = low; j <= high - 1; j++)
		{
			if(list.get(j).getValue() < pivot)
			{
				i++;
				Collections.swap(list, i, j);
			}
		}

		Collections.swap(list, i + 1, high);

		return i + 1;
	}

	static void swap(int[] arr, int i, int j)
	{

		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public static void quickSort(List<Map.Entry<String, Integer>> list, int low, int high)
	{
		if(low < high)
		{
			int pi = partition(list, low, high);

			quickSort(list, low, pi - 1);
			quickSort(list, pi + 1, high);
		}
	}
}