from transformers import AutoTokenizer
import json

# Load the same model tokenizer used for embeddings
tokenizer = AutoTokenizer.from_pretrained("sentence-transformers/all-MiniLM-L6-v2")

# Your query string (can be dynamic)
query = "Java developer skills"

# Tokenize with max length (pad/truncate)
tokens = tokenizer(
    query,
    return_tensors="np",     # Return NumPy arrays
    padding="max_length",    # Pad to fixed length
    truncation=True,         # Truncate if too long
    max_length=32            # Max token length
)


# Convert numpy arrays to Python lists
input_ids = tokens['input_ids'].tolist()[0]
attention_mask = tokens['attention_mask'].tolist()[0]

# Save to JSON (for Java to read)
with open("query_tokens.json", "w") as f:
    json.dump({"input_ids": input_ids, "attention_mask": attention_mask}, f)
mvn

print("Tokenization saved to query_tokens.json")
