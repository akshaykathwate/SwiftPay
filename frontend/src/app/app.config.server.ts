import { mergeApplicationConfig, ApplicationConfig } from '@angular/core';
import { provideServerRendering } from '@angular/platform-server';
import { appConfig } from './app.config';
import { BrowserModule } from '@angular/platform-browser';
import { importProvidersFrom } from '@angular/core';


const serverConfig: ApplicationConfig = {
  providers: [provideServerRendering(), importProvidersFrom(BrowserModule)],
};

export const config = mergeApplicationConfig(appConfig, serverConfig);
