using Microsoft.AspNetCore.Mvc;

namespace stockapi.Controllers;

[ApiController]
[Route("api")]
public class StockController : ControllerBase
{
  private readonly StockService _service;
  public StockController(StockService service)
  {
    _service = service;
  }

  [NonAction]
  public IActionResult Result(object result)
    => result == null ? BadRequest() : Ok(result);


  [HttpGet("health")]
  public IActionResult Health()
    => Ok("ok");

  [HttpGet("master")]
  public IActionResult GetMasterList()
    => Result(_service.GetMasterList());

  [HttpGet("master/{masterUrl}")]
  public IActionResult GetMaster(string masterUrl)
    => Result(_service.GetMaster(masterUrl));

  [HttpGet("match")]
  public IActionResult GetMatchList()
  => Result(_service.GetMatchList());

  [HttpGet("match/{matchUrl}")]
  public IActionResult GetMatch(string matchUrl)
    => Result(_service.GetMatch(matchUrl));

  [HttpGet("login")]
  public IActionResult Login(string id, string password)
    => Result(_service.Login(id, password));

  [HttpPost("project")]
  public IActionResult CreateProject(CreateProjectReq req)
  {
    var result = _service.CreateProject(req);
    return result == 1 ? Ok(result) : BadRequest(result);
  }

  [HttpGet("project")]
  public IActionResult GetProjects()
    => Result(_service.GetProjects());

  [HttpGet("project/{projectName}")]
  public IActionResult GetProject(string projectName)
  => Result(_service.GetProject(projectName));


  [HttpGet("project_for_work/{projectName}")]
  public IActionResult GetProjectForWork(string projectName)
    => Result(_service.GetProjectForWork(projectName));

  [HttpPost("work/append")]
  public IActionResult AppendWork(AppendWorkReq req)
    => Result(_service.AppendWork(req));

  [HttpGet("work/collect/{projectName}")]
  public IActionResult CollectWork(string projectName)
    => Result(_service.Collect(projectName));

}