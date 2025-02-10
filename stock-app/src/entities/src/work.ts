import { Sheet, SectionSheet } from '../internal';


export interface Work {
  id : string;
  projectName : string;
  masterUrl? : string;
  mappingUrl? : string;
  sections : SectionSheet;
}

export type WorkSheet = Sheet<Work>;
