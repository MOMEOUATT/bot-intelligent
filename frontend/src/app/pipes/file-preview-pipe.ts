import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Pipe({
  name: 'filePreview'
})
export class FilePreviewPipe implements PipeTransform {

  constructor(private sanitizer: DomSanitizer){}

  transform(file: File): SafeUrl {
    const url = URL.createObjectURL(file);
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

}
