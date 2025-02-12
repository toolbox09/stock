import { Sheet, SectionSheet } from '../internal';
import { KeyNum } from './key-num';

export interface Work {
  id : string;
  projectName : string;
  master? : KeyNum;
  // match? : KeyNum;
  sections : SectionSheet;
}

export type WorkSheet = Sheet<Work>;
