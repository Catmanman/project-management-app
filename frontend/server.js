// server.js
const express = require('express');
const path = require('path');
const app = express();

 
const distDir = path.join(__dirname, 'dist', 'project-management-frontend', 'browser');

app.use(express.static(distDir, { maxAge: '1y', etag: false }));

 
app.get('*', (_, res) => res.sendFile(path.join(distDir, 'index.html')));

const port = process.env.PORT || 4200;
app.listen(port, () => console.log(`SPA listening on ${port}`));
