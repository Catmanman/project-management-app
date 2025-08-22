import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

// Bootstraps the Angular application using the standalone API.  Any
// initialisation errors are caught and logged to the console.  See
// app.config.ts for providers and interceptors.  The root component
// lives in app.ts and defines the router outlet.
bootstrapApplication(App, appConfig).catch((err) => console.error(err));