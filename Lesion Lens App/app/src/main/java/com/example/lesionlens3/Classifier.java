package com.example.lesionlens3;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

//import androidx.room.jarjarred.org.antlr.v4.gui.Interpreter;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Classifier {

    private Interpreter interpreter;
    private List<String> labelList;
    private final int inputSize;
    private final int pixelSize = 3;
    private final int imageMean = 0;
    private final float imageStd = 255.0f;
    private final int maxResults = 3;
    private final float threshold = 0.65f;

    public static class Recognition {
        public String id = "";
        public String title = "";
        public float confidence = 0;

        public Recognition(String id, String title, float confidence) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
        }
        @Override
        public String toString() {
            return "Title = " + title + ", Confidence = " + confidence;
        }
    }

    public Classifier(AssetManager assetManager, String modelPath, String labelPath, int inputSize) {
        this.interpreter = new Interpreter(loadModelFile(assetManager, modelPath));
        this.labelList = loadLabelList(assetManager, labelPath);
        this.inputSize = inputSize;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(assetManager.openFd(modelPath).getFileDescriptor());
            FileChannel fileChannel = fileInputStream.getChannel();
            long startOffset = assetManager.openFd(modelPath).getStartOffset();
            long declaredLength = assetManager.openFd(modelPath).getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) {
        List<String> labelList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labelList;
    }

    public List<Recognition> recognizeImage(Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false);

        ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);
        System.out.println("ahkjdbhjdsgfdufahufsdbfuudfhusidfhdasuoifhdsao"+labelList.size());
        float[][] result = new float[1][labelList.size()];
        interpreter.run(byteBuffer, result);
        return getSortedResult(result);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * pixelSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[inputSize * inputSize];

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF) - imageMean) / imageStd);
                byteBuffer.putFloat((((val >> 8) & 0xFF) - imageMean) / imageStd);
                byteBuffer.putFloat(((val & 0xFF) - imageMean) / imageStd);
            }
        }
        return byteBuffer;
    }

    private List<Recognition> getSortedResult(float[][] labelProbArray) {
        PriorityQueue<Recognition> pq = new PriorityQueue<>(
                maxResults,
                (Recognition r1, Recognition r2) -> Float.compare(r2.confidence, r1.confidence)
        );
        float maxConfidence = 0.0f; // Initialize maxConfidence to 0

        for (int i = 0; i < labelList.size(); ++i) {
            float confidence = labelProbArray[0][i];
            if (confidence >= threshold) {
                pq.add(new Recognition("" + i, (i < labelList.size()) ? labelList.get(i) : "Unknown", confidence));
            }

            // Update maxConfidence if the current confidence is higher
            maxConfidence = Math.max(maxConfidence, confidence);
        }

        if (maxConfidence < threshold) {
            // Add "Not Skin Image" recognition if the maximum confidence is below the threshold
            pq.add(new Recognition("", "Not Skin Image", maxConfidence));
        }




        List<Recognition> recognitions = new ArrayList<>();
        int recognitionsSize = Math.min(pq.size(), maxResults);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }
}


