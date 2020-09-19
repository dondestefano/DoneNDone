const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const admin = require('firebase-admin');
const { onRequest } = require('firebase-functions/lib/providers/https');

admin.initializeApp();
const app = express();
const db = admin.firestore();

/*
/*/
/* Endpoints *//*
/
exports.user = functions.https.onRequest(app);

/*/
/* Collection references *//*
/
const userCollection = db.collection("users")

// Get all users.
app.get('/', async (req, res) => {
    const result = await userCollection.get();

    // Create an array for users.
    let users = [];
    result.forEach((doc) => {
        const id = doc.id;
        const data = doc.data();
        users.push({ id, ...data});
    });
    res.status(200).send(JSON.stringify(users));
});

// Get single user
app.get('/:id', async (req, res) => {
    const result = await userCollection.doc(req.params.id).get();

    const id = result.id;
    const user = result.data();

    res.status(200).send({ id, ...user });
});

// Create user
app.post('/', async (req, res) => {
    const user = req.body;
    const id = user.id;
    const result = await userCollection.add(user);

    res.status(200).send(user);
});

// Update user
app.put('/:id', async (req, res) => {
    const body = req.body;
    cons userToUpdate = userCollection.doc(req.params.id)
    const result = userToUpdate
      .update(body);

    res.status(200).send(userToUpdate);
});

// Delete user
app.delete('/:id', async (req, res) => {
  await userCollection.doc(req.params.id).delete();

  res.status(200).send();
});
*/


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

    res.status(200).send(post);
});

// Update post
app.put('/:id', async (req, res) => {
    const body = req.body;
    const postToUpdate = postCollection.doc(req.params.id)
    const result = await postToUpdate.update(body);

    res.status(200).send(postCollection.doc(req.params.id));
});

// Delete post
app.delete('/:id', async (req, res) => {
  await postCollection.doc(req.params.id).delete();

  res.status(200).send();
});