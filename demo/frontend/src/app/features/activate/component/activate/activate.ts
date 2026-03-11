import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from "@angular/core";
import { ActivateService } from "../../service/activate-service";
import { ActivatedRoute, RouterModule } from "@angular/router";
import { delay, Subscription } from "rxjs";

@Component({
  selector: "app-activate",
  imports: [CommonModule,RouterModule],
  templateUrl: "./activate.html",
  styleUrl: "./activate.css",
  changeDetection: ChangeDetectionStrategy.OnPush  // <-- this is the culprit

})
export class ActivateComponent implements OnInit, OnDestroy {
  errorMessage: string = '';
  activationState: null | 'success' | 'error' = null;
  isLoading: boolean = false;
  isResending: boolean = false;

  private username: string | null = null;
  private sub: Subscription | null = null;

  constructor(
    private route: ActivatedRoute,
    private activateService: ActivateService
  ) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username');
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  onActivate(): void {
    if (!this.username) {
      this.activationState = 'error';
      this.errorMessage = 'Invalid activation link.';
      return;
    }
    this.sub?.unsubscribe(); // prevent overlapping subscriptions on retry
    this.errorMessage = '';

    this.sub = this.activateService.activate(this.username).subscribe({
      next: () => {
        this.activationState = 'success';
        this.isLoading = false;
      },
      error: (err) => {
        this.activationState = 'error';
        this.errorMessage = err?.error?.message ?? 'Activation failed.';
        this.isLoading = false;
      }
    });
  }

  onRetry(): void {
    this.activationState = null;   // back to default state
    this.errorMessage = '';
    this.onResend();
  }

  onResend(): void {
    if(!this.username) return;
    this.sub = this.activateService.activate(this.username).subscribe({
      next: () => {
        this.activationState = 'success';
        this.isLoading = false;
      },
      error: (err) => {
        this.activationState = 'error';
        this.errorMessage = err?.error?.message ?? 'Activation failed.';
        this.isLoading = false;
      }
    });
  }
}