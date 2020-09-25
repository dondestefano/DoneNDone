const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const admin = require('firebase-admin');
const { onRequest } = require('firebase-functions/lib/providers/https');

admin.initializeApp();
const app = express();
const db = admin.firestore();
app.use(cors({ origin: ['http://localhost:5000'] }));

exports.post = functions.https.onRequest(app);

//* Collection references *//
const postCollection = db.collection("posts")

//Get all posts.
app.get('/', async (req, res) => {
    try {
        const result = await postCollection.get();

        // Create an array of posts.
        let posts = [];
        result.forEach((doc) => {
            const id = doc.id;
            const data = doc.data();
            posts.push({ id, ...data});
        });

        return res.status(200).send(JSON.stringify(posts));
    }
    catch(error) {
        return res.status(500).send(error.message)
    }

});

//Get a single post
app.get('/:id', async (req, res) => {
  try {
    const result = await postCollection.doc(req.params.id).get();

    if(result.exists) {
      const id = result.id;
      const post = result.data();

      return res.status(200).send({ id, ...post });
    }
    else {
      return res.status(404).send();
    }
  }
  catch (error) {
    return res.status(500).send(error.message);
  }
});

//Create post
app.post('/', async (req, res) => {
try {
    const post = (req.body)

    if (!post.title) {
       res.status(400).send("Title is required.");
    }

    let docRef = postCollection.doc();
    //Add ID to the post after it has been created.
    const newPost = {
        ...post,
        id: docRef.id
    }
    await docRef.set(newPost);

    res.status(201).send(newPost);
}
catch(error) {
    console.log(error);
    return res.status(500).send(error.message);
}

});

//Update post
app.put('/:id', async (req, res) => {
    const body = req.body;
    const postToUpdate = postCollection.doc(req.params.id)
    const result = await postToUpdate.update(body);

    res.status(200).send(body);
});

//Delete post
app.delete('/:id', async (req, res) => {
  try {
    const userRef = postCollection.doc(req.params.id);
    const user = await userRef.get()

    if (user.exists) {
      await user.ref.delete();
      return res.status(200).send();
    }

    return res.status(404).send();

  } catch(error) {
    console.log(error);
    return res.status(500).send(error.message);
  }
});