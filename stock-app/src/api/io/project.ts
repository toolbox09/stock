import { BarcodeRaw } from '@/entities';

export interface CreateProjectRequest {
  name : string;
  masterUrl? : string;
  matchUrl? : string;
}

export interface UpdateWorkRequest {
  projectName : string;
  fileName : string;
  raws : BarcodeRaw[];
}
