const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const admin = require('firebase-admin');
const { onRequest } = require('firebase-functions/lib/providers/https');

admin.initializeApp();
const app = express();
const db = admin.firestore();

// Post functions

//* Endpoint *//
exports.post = functions.https.onRequest(app);

//* Collection references *//
const postCollection = db.collection("posts")

// Get all posts.
app.get('/', async (req, res) => {
    const result = await postCollection.get();

    // Create an array of posts.
    let posts = [];
    result.forEach((doc) => {
        const id = doc.id;
        const data = doc.data();
        posts.push({ id, ...data});
    });
    res.status(200).send(JSON.stringify(posts));
});

// Get a single post
app.get('/:id', async (req, res) => {
    const result = await postCollection.doc(req.params.id).get();

    const id = result.id;
    const post = result.data();

    res.status(200).send({ id, ...post });
});

// Create post
app.post('/', async (req, res) => {
    const post = req.body;
    let docRef = await postCollection.doc();
    await docRef.set({
        ...post,
        id: docRef.id,
        });

    res.status(200).send(docRef);
});

// Update post
app.put('/:id', async (req, res) => {
    const body = req.body;
    const postToUpdate = postCollection.doc(req.params.id)
    const result = await postToUpdate.update(body);

    res.status(200).send(body);
});

// Delete post
app.delete('/:id', async (req, res) => {
  await postCollection.doc(req.params.id).delete();

  res.status(200).send();
});