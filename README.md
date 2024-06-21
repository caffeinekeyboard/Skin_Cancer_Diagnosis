
# Skin Cancer Diagnosis using Supervised Residual Neural Networks.

In this project, we have experimented with two of the most popular residual neural networks, with drastically different parameter counts. We observed that the increase in the number of parameters is disproportionate to the increase in accuracy of classification.

We trained InceptionResNet-v2 and ResNet-50 on the Human Against Machine - 10000 Dataset, collected by Harvard. 


## Performance:

| Model | Parameters | Accuracy |
|---|---|---|
| ResNet-50 | 23M | 76.28% |
| InceptionResNet-v2 | 55M | 89.02% |
![Final Confusion Matrix](https://github.com/caffeinekeyboard/Skin_Cancer_Diagnosis/assets/96489029/0dda8c7a-7daa-4779-b879-08f5b56a95bd)



## Related Material:

[Dataset](https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/DBW86T)\
[InceptionResNet Paper](https://arxiv.org/abs/1602.07261)\
[ResNet Paper](https://arxiv.org/abs/1512.03385)

