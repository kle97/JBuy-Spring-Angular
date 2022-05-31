import { Directive, Input, TemplateRef, ViewContainerRef } from "@angular/core";

interface VarContext {
  $implicit: unknown;
  appVar: unknown;
}

@Directive({
  selector: '[appVar]'
})
export class VarDirective {

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
  ) {
  }

  private context: VarContext = {
    $implicit: null,
    appVar: null,
  };

  private hasView = false;

  @Input() set appVar(context: unknown) {
    this.context.$implicit = this.context.appVar = context;

    if (!this.hasView) {
      this.viewContainer.createEmbeddedView(this.templateRef, this.context);
      this.hasView = true;
    }
  }

}
