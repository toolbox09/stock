import { Sheet, BarcodeSheet } from '../internal';


export interface Section {
  id : string;
  name : string;
  created : string;
  barcodes : BarcodeSheet;
  selected? : boolean;
}

export type SectionSheet = Sheet<Section>;