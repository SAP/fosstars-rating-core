import pandas as pd
import matplotlib.pyplot as plt
import textwrap
import yaml


# draw histograms
def draw_hists(columns, test_vectors, width=20, height=5):
    n = len(columns)
    fig, axes = plt.subplots(1, n)
    fig.set_figwidth(width)
    fig.set_figheight(height)
    i = 0
    for column in columns:
        test_vectors[column].value_counts().plot(kind='bar', rot=0, ax=axes[i])
        axes[i].set_title(textwrap.TextWrapper(width=25).fill(column))
        i = i + 1
      
    
# load a list of test vectors from a YAML file
def load_test_vectors_from_yaml(file, features):
    rows = []
    with open(file, 'r') as f:
        raw_data = yaml.load(f)
        for raw in raw_data['elements']:
            data = {}
            data['alias'] = raw['alias']
            for value in raw['values']:
                feature_type = value['type']
                if feature_type == 'ScoreValue':
                    feature_value = value['value']
                elif feature_type == 'BooleanValue':
                    feature_value = value['flag']
                elif feature_type == 'DateValue':
                    feature_value = value['date']
                elif feature_type == 'IntegerValue' or feature_type == 'PositiveIntegerValue':
                    feature_value = value['number']
                elif feature_type == 'LgtmGradeValue':
                    feature_value = value['value']
                elif feature_type == 'UnknownValue':
                    feature_value = 'unknown'
                elif feature_type == 'VulnerabilitiesValue':
                    feature_value = '...'
                else:
                    raise Exception('Oh no! Unknown type: ' + feature_type)
                if feature_type == 'ScoreValue':
                    feature_name = value['score']['name']
                else:
                    feature_name = value['feature']['name']
                if feature_name not in features:
                    raise Exception('Oh no! Unknown feature: ' + feature_name)
                data[feature_name] = feature_value
            data['score_from'] = raw['expectedScore']['from']
            data['score_to'] = raw['expectedScore']['to']
            rows.append(data)
    return pd.DataFrame(rows)
