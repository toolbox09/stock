import { ProjectInfo } from './project-info';
import { FileInfo } from './file-info';

export interface ProjectState extends ProjectInfo {
  originFile : FileInfo;
  mergeFile : FileInfo;
  workFiles : FileInfo[];
}