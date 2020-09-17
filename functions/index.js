const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const admin = require('firebase-admin');
const { onRequest } = require('firebase-functions/lib/providers/https');

admin.initializeApp();
const app = express();
const db = admin.firestore();

//* Endpoints *//
exports.user = functions.https.onRequest(app);

//* Collection references *//
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
    await userCollection.add(user);

    res.status(200).send();
});

// Update user
app.put('/:id', async (req, res) => {
    const body = req.body;
    const result = await userCollection
      .doc(req.params.id)
      .update(body);

    res.status(200).send(result);
});

// Delete user
app.delete('/:id', async (req, res) => {
  await userCollection.doc(req.params.id).delete();

  res.status(200).send();
});