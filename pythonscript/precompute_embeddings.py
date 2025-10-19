from sentence_transformers import SentenceTransformer
import mysql.connector
import json

# Connect to your DB
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="12345678",
    database="portfolio"
)
cursor = conn.cursor(dictionary=True)
model = SentenceTransformer('all-MiniLM-L6-v2')
embeddings_data = []
# Users
cursor.execute("SELECT id, name, title, about_me FROM user_profile")
for row in cursor.fetchall():
    text = f"User: {row['name']}, Title: {row['title']}, About: {row['about_me']}"
    embedding = model.encode(text).tolist()
    embeddings_data.append({"id": row['id'], "type": "user", "text": text, "embedding": embedding})

# Skills
cursor.execute("SELECT id, skill_name, category FROM skills")
for row in cursor.fetchall():
    text = f"Skill: {row['skill_name']} ({row['category']})"
    embedding = model.encode(text).tolist()
    embeddings_data.append({"id": row['id'], "type": "skill", "text": text, "embedding": embedding})

# Projects
cursor.execute("SELECT id, title, description, tech_stack FROM projects")
for row in cursor.fetchall():
    text = f"Project: {row['title']}, Description: {row['description']}, Tech: {row['tech_stack']}"
    embedding = model.encode(text).tolist()
    embeddings_data.append({"id": row['id'], "type": "project", "text": text, "embedding": embedding})

# Education
cursor.execute("SELECT id, degree, university, year FROM education")
for row in cursor.fetchall():
    text = f"Education: {row['degree']} from {row['university']} ({row['year']})"
    embedding = model.encode(text).tolist()
    embeddings_data.append({"id": row['id'], "type": "education", "text": text, "embedding": embedding})

# Experience
cursor.execute("SELECT id, role, company, description FROM experience")
for row in cursor.fetchall():
    text = f"Experience: {row['role']} at {row['company']}, {row['description']}"
    embedding = model.encode(text).tolist()
    embeddings_data.append({"id": row['id'], "type": "experience", "text": text, "embedding": embedding})

# Save to JSON
with open("embeddings.json", "w", encoding="utf-8") as f:
    json.dump(embeddings_data, f, ensure_ascii=False, indent=2)

print("Embeddings saved to embeddings.json")
