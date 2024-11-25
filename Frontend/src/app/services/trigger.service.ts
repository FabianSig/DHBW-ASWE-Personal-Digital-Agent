import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ChatService } from './chat.service';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerMap: { [url: string]: number } = {};
  private timoutReferenceMap: { [url: string]: ReturnType<typeof setTimeout> } = {};

  constructor(
    private apiService: ApiService,
    private chatService: ChatService
  ) {
    this.reload();
  }

  currentDate = new Date().toISOString();

  setOffTrigger() {
    this.apiService.getTriggerData(this.currentDate).pipe(
      catchError(error => {
        console.error('Error fetching trigger data', error);
        return of({ triggers: [] });
      })
    ).subscribe((data: any) => {
      if (data && data.triggers) {
        data.triggers.map((trigger: any) => {
          this.triggerMap[trigger.route] = new Date(trigger.time).getTime();
        });
        this.processTriggers();
      } else {
        console.error('Received invalid trigger data', data);
      }
    });
  }

  private processTriggers() {
    let currentTimeInMs = new Date().getTime(); // Aktuelle Zeit in Millisekunden

    Object.entries(this.triggerMap).forEach(([routine, triggerTime]) => {
      const timeDifference = triggerTime - currentTimeInMs; // Berechnung der Differenz in Millisekunden

      // Wenn die Zeit in der Zukunft liegt (negative Differenz), nichts tun
      if (timeDifference >= 0) {
        // Wenn die Zeit in der Zukunft liegt (positive Differenz), Timer setzen
        this.timoutReferenceMap[routine] =  setTimeout(() => {
          this.executeRoutine(routine);
        }, timeDifference); // Ausführen der Routine nach der berechneten Differenz
      }
    });
  }

  private executeRoutine(url: string) {
    this.apiService.executeCustomTriggerRoutine(url).pipe(
      catchError(error => {
        console.error('Error executing custom trigger routine', error);
        return of('Error executing trigger');
      })
    ).subscribe((response: string) => {
      this.chatService.addMessage(response, 'chatgpt');
    });
  }

  private clearAllTriggers() {
    // Leere alle aktiven Timer
    Object.entries(this.timoutReferenceMap).forEach(([routine, timeoutRef]) => {
      clearTimeout(timeoutRef);
    });

    this.timoutReferenceMap = {}; // Leere das Referenzobjekt
  }

  public reload(): void {
    this.clearAllTriggers();
    this.setOffTrigger();
  }
}
